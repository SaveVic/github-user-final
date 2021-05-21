package com.example.githubuserfinal.data.db

import android.content.ContentValues
import android.database.Cursor
import com.example.githubuserfinal.data.db.DatabaseContract.StoredColumn.Companion.AVATAR
import com.example.githubuserfinal.data.db.DatabaseContract.StoredColumn.Companion.USERNAME
import com.example.githubuserfinal.data.db.DatabaseContract.StoredColumn.Companion._ID
import com.example.githubuserfinal.data.model.StoredQueryUser

fun ContentValues.toStored() = StoredQueryUser(
    id = getAsInteger(_ID),
    username = getAsString(USERNAME),
    avatar_url = getAsString(AVATAR),
)

fun StoredQueryUser.toContentVal() = ContentValues().apply {
    put(_ID, id)
    put(USERNAME, username)
    put(AVATAR, avatar_url)
}

fun Cursor.toStored() = StoredQueryUser(
    id = getInt(getColumnIndexOrThrow(_ID)),
    username = getString(getColumnIndexOrThrow(USERNAME)),
    avatar_url = getString(getColumnIndexOrThrow(AVATAR)),
)