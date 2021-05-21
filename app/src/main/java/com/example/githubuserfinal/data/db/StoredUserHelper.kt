package com.example.githubuserfinal.data.db

import android.content.Context
import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubuserfinal.data.model.StoredQueryUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class StoredUserHelper(private val context: Context) {
    private var db: StoredUserDatabase? = null

    private fun initDB() = StoredUserDatabase.getDB(context)

    fun getAll(): Cursor {
        db = initDB()
        return db!!.storedUserDao().getAll()
    }

    fun getSelected(id: Int): Cursor{
        db = initDB()
        return db!!.storedUserDao().getSelected(id)
    }

    fun insert(user: StoredQueryUser): Long{
        db = initDB()
        return db!!.storedUserDao().insert(user)
//        CoroutineScope(IO).launch {
//            db!!.storedUserDao().insert(user)
//        }
    }

    fun delete(id: Int): Int{
        db = initDB()
        return db!!.storedUserDao().delete(id)
//        CoroutineScope(IO).launch {
//            db!!.storedUserDao().delete(user)
//        }
    }
}