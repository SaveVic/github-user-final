package com.example.consumerapp.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.consumerapp.data.db.DatabaseContract.StoredColumn.Companion.CONTENT_URI
import com.example.consumerapp.data.db.StoredUserHelper
import com.example.consumerapp.data.db.toStored
import com.example.consumerapp.data.model.StoredQueryUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class FavoriteRepository{
    fun getAllStored(context: Context): LiveData<List<StoredQueryUser>> {
        val data = MutableLiveData<List<StoredQueryUser>>()

        CoroutineScope(IO).launch {
            val cursor = context.contentResolver
                .query(CONTENT_URI, null, null, null, null)
            cursor?.let {
                val result = arrayListOf<StoredQueryUser>()
                while(it.moveToNext()){
                    result.add(it.toStored())
                }
                data.postValue(result)
                cursor.close()
            }
        }
        return data
    }
}