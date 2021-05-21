package com.example.githubuserfinal.data.db

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubuserfinal.data.db.DatabaseContract.StoredColumn.Companion.TABLE_NAME
import com.example.githubuserfinal.data.db.DatabaseContract.StoredColumn.Companion.USERNAME
import com.example.githubuserfinal.data.db.DatabaseContract.StoredColumn.Companion._ID
import com.example.githubuserfinal.data.model.StoredQueryUser

@Dao
interface StoredUserDAO {
    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): Cursor

    @Query("SELECT * FROM $TABLE_NAME WHERE $_ID = :id")
    fun getSelected(id: Int): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: StoredQueryUser): Long

    @Query("DELETE FROM $TABLE_NAME WHERE $_ID = :id")
    fun delete(id: Int): Int
}