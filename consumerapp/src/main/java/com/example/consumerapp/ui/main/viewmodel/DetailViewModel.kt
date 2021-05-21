package com.example.consumerapp.ui.main.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.consumerapp.data.model.QueryUser
import com.example.consumerapp.data.model.StoredQueryUser
import com.example.consumerapp.data.model.User
import com.example.consumerapp.data.repository.DetailRepository
import com.example.consumerapp.utils.ResourceQuery
import com.example.consumerapp.utils.ResourceUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val detailRepository: DetailRepository, private val user: StoredQueryUser) : ViewModel() {
    private val infoUser = MutableLiveData<ResourceUser<User>>()
    private val infoFollowersList = MutableLiveData<ResourceQuery<List<QueryUser>>>()
    private val infoFollowingsList = MutableLiveData<ResourceQuery<List<QueryUser>>>()

//    val isStored = MutableLiveData<Boolean>()
    private lateinit var getStored: MutableLiveData<Boolean>

    init {
        fetchData()
        fetchFollowings()
        fetchFollowers()
    }

    private fun fetchData() {
        infoUser.postValue(ResourceUser.loading(null))
        val call = detailRepository.getDetailUser(user.username)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful){
                    val result = response.body()!!
                    infoUser.postValue(ResourceUser.success(result))
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                infoUser.postValue(
                    ResourceUser.error(
                        null,
                        t.message ?: ""
                    )
                )
            }
        })
    }

    private fun fetchFollowings() {
        infoFollowingsList.postValue(ResourceQuery.loading(null))
        val call = detailRepository.getUserFollowings(user.username)
        call.enqueue(object : Callback<List<QueryUser>> {
            override fun onResponse(call: Call<List<QueryUser>>, response: Response<List<QueryUser>>) {
                if (response.isSuccessful){
                    val result = response.body()!!
                    when (result.size) {
                        0 -> infoFollowingsList.postValue(ResourceQuery.empty(null))
                        else -> infoFollowingsList.postValue(ResourceQuery.filled(result))
                    }
                }
            }
            override fun onFailure(call: Call<List<QueryUser>>, t: Throwable) {
                infoFollowingsList.postValue(
                    ResourceQuery.error(
                        null,
                        t.message ?: ""
                    )
                )
            }
        })
    }

    private fun fetchFollowers() {
        infoFollowersList.postValue(ResourceQuery.loading(null))
        val call = detailRepository.getUserFollowers(user.username)
        call.enqueue(object : Callback<List<QueryUser>> {
            override fun onResponse(call: Call<List<QueryUser>>, response: Response<List<QueryUser>>) {
                if (response.isSuccessful){
                    val result = response.body()!!
                    when (result.size) {
                        0 -> infoFollowersList.postValue(ResourceQuery.empty(null))
                        else -> infoFollowersList.postValue(ResourceQuery.filled(result))
                    }
                }
            }
            override fun onFailure(call: Call<List<QueryUser>>, t: Throwable) {
                infoFollowersList.postValue(
                    ResourceQuery.error(
                        null,
                        t.message ?: ""
                    )
                )
            }
        })
    }

    fun getInfoUser(): LiveData<ResourceUser<User>> {
        return infoUser
    }

//    fun isStored(context: Context) = detailRepository.checkStored(context, user.id)

    fun isStored(): LiveData<Boolean> = getStored

    fun initStored(context: Context){
        getStored = detailRepository.checkStored(context, user.id)
    }

    fun setStored(context: Context, user: StoredQueryUser, liked: Boolean){
        CoroutineScope(IO).launch {
            when(liked){
                true -> detailRepository.setStored(context, user)
                false -> detailRepository.deleteStored(context, user)
            }
            getStored.postValue(liked)
        }
    }

    fun getFollowersList(): LiveData<ResourceQuery<List<QueryUser>>> {
        return infoFollowersList
    }

    fun getFollowingsList(): LiveData<ResourceQuery<List<QueryUser>>> {
        return infoFollowingsList
    }
}