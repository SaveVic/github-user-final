package com.example.consumerapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.consumerapp.data.model.StoredQueryUser

@Database(entities = [StoredQueryUser::class], version = 2, exportSchema = false)
abstract class StoredUserDatabase : RoomDatabase() {
    abstract fun storedUserDao(): StoredUserDAO

    companion object {
        @Volatile
        private var INSTANCE: StoredUserDatabase? = null

        fun getDB(context: Context): StoredUserDatabase {
            if (INSTANCE != null) return INSTANCE!!

            synchronized(this) {
                INSTANCE = Room.databaseBuilder(context, StoredUserDatabase::class.java, "USER_DB")
                    .fallbackToDestructiveMigration().build()
                return INSTANCE!!
            }
        }
    }
}