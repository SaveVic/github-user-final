package com.example.githubuserfinal.data.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.githubuserfinal.data.db.DatabaseContract.AUTHORITY
import com.example.githubuserfinal.data.db.DatabaseContract.StoredColumn.Companion.CONTENT_URI
import com.example.githubuserfinal.data.db.DatabaseContract.StoredColumn.Companion.TABLE_NAME
import com.example.githubuserfinal.data.db.StoredUserHelper
import com.example.githubuserfinal.data.db.toStored

class StoredProvider : ContentProvider() {

    private lateinit var helper: StoredUserHelper

    companion object {
        private const val STORED = 1
        private const val STORED_ID = 2
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(AUTHORITY, TABLE_NAME, STORED)
            uriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", STORED_ID)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (STORED_ID) {
            uriMatcher.match(uri) -> uri.lastPathSegment?.toInt()?.let { helper.delete(it) } ?: 0
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (STORED) {
            uriMatcher.match(uri) -> values?.toStored()?.let { helper.insert(it) } ?: 0
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun onCreate(): Boolean {
        helper = StoredUserHelper(context as Context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            STORED -> helper.getAll()
            STORED_ID -> uri.lastPathSegment?.toInt()?.let { helper.getSelected(it) }
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int = 0
}