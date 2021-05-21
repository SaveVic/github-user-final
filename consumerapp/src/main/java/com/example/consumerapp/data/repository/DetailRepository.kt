package com.example.consumerapp.data.repository

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.consumerapp.data.api.ApiHelper
import com.example.consumerapp.data.db.DatabaseContract.StoredColumn.Companion.CONTENT_URI
import com.example.consumerapp.data.db.StoredUserHelper
import com.example.consumerapp.data.db.toContentVal
import com.example.consumerapp.data.db.toStored
import com.example.consumerapp.data.model.QueryUser
import com.example.consumerapp.data.model.StoredQueryUser
import com.example.consumerapp.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call

class DetailRepository(private val apiHelper: ApiHelper) {
    fun getDetailUser(username: String): Call<User> {
        return apiHelper.getDetailUser(username)
    }

    fun checkStored(context: Context, id: Int): MutableLiveData<Boolean>{
        val isStored = MutableLiveData<Boolean>()
        isStored.postValue(false)
        val uri = "$CONTENT_URI/$id".toUri()

        CoroutineScope(IO).launch{
            val cursor = context.contentResolver
                .query(uri, null, null, null, null)
            cursor?.let {
                if (cursor.moveToFirst()) {
                    isStored.postValue(true)
                } else {
                    isStored.postValue(false)
                }
                cursor.close()
            }
        }
        return isStored
    }

    fun setStored(context: Context, stored: StoredQueryUser): LiveData<Long>{
        val done = MutableLiveData<Long>()

        val cursor =
            context.contentResolver.insert(CONTENT_URI, stored.toContentVal())

        cursor?.let { done.postValue(1) }

        return done
    }

    fun deleteStored(context: Context, stored: StoredQueryUser): LiveData<Long>{
        val done = MutableLiveData<Long>()

        val cursor =
            context.contentResolver.delete("$CONTENT_URI/${stored.id}".toUri(), null, null)

        cursor.let { done.postValue(it.toLong()) }

        return done
    }

    fun getUserFollowers(username: String): Call<List<QueryUser>> {
        return apiHelper.getUserFollowers(username)
    }

    fun getUserFollowings(username: String): Call<List<QueryUser>> {
        return apiHelper.getUserFollowings(username)
    }
}