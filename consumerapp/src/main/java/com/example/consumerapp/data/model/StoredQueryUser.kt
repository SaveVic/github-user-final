package com.example.consumerapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.consumerapp.data.db.DatabaseContract

@Entity(tableName = DatabaseContract.StoredColumn.TABLE_NAME)
data class StoredQueryUser(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = DatabaseContract.StoredColumn._ID) val id: Int,
    @ColumnInfo(name = DatabaseContract.StoredColumn.USERNAME) val username: String,
    @ColumnInfo(name = DatabaseContract.StoredColumn.AVATAR) val avatar_url: String,
)
