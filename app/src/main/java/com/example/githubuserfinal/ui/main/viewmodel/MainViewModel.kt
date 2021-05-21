package com.example.githubuserfinal.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserfinal.data.model.QueryResult
import com.example.githubuserfinal.data.model.QueryUser
import com.example.githubuserfinal.data.repository.MainRepository
import com.example.githubuserfinal.utils.ResourceQuery
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val queryListUsers = MutableLiveData<ResourceQuery<List<QueryUser>>>()

    init {
        initialQuery()
    }

    private fun initialQuery() {
        queryListUsers.postValue(ResourceQuery.empty(null))
    }

    fun fetchQuery(query: String) {
        queryListUsers.postValue(ResourceQuery.loading(null))
        val call = mainRepository.getQueryUserResult(query)
        call.enqueue(object : Callback<QueryResult> {
            override fun onResponse(call: Call<QueryResult>, response: Response<QueryResult>) {
                if (response.isSuccessful){
                    val result = response.body()!!
                    when (result.total_count) {
                        0 -> queryListUsers.postValue(ResourceQuery.empty(null))
                        else -> queryListUsers.postValue(ResourceQuery.filled(result.items))
                    }
                }
            }
            override fun onFailure(call: Call<QueryResult>, t: Throwable) {
                queryListUsers.postValue(
                    ResourceQuery.error(
                        null,
                        t.message ?: ""
                    )
                )
            }
        })
    }

    fun getListUsers(): LiveData<ResourceQuery<List<QueryUser>>> {
        return queryListUsers
    }

}