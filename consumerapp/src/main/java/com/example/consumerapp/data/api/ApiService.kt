package com.example.consumerapp.data.api

import com.example.consumerapp.data.model.QueryResult
import com.example.consumerapp.data.model.QueryUser
import com.example.consumerapp.data.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

const val TOKEN = "YOUR TOKEN"

interface ApiService {

    @Headers("Authorization: token $TOKEN")
    @GET("/search/users")
    fun getQueryUserResult(@Query("q") query: String): Call<QueryResult>

    @Headers("Authorization: token $TOKEN")
    @GET("/users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<User>

    @Headers("Authorization: token $TOKEN")
    @GET("/users/{username}/followers")
    fun getUserFollowers(@Path("username") username: String): Call<List<QueryUser>>

    @Headers("Authorization: token $TOKEN")
    @GET("/users/{username}/following")
    fun getUserFollowings(@Path("username") username: String): Call<List<QueryUser>>
}