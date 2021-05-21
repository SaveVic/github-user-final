package com.example.githubuserfinal.data.repository

import com.example.githubuserfinal.data.api.ApiHelper
import com.example.githubuserfinal.data.model.QueryResult
import retrofit2.Call

class MainRepository(private val apiHelper: ApiHelper) {

    fun getQueryUserResult(query: String): Call<QueryResult> {
        return apiHelper.getQueryUserResult(query)
    }
}