package com.example.consumerapp.data.api

class ApiHelper {
    private val service = ApiBuilder.service

    fun getQueryUserResult(query: String) = service.getQueryUserResult(query)

    fun getDetailUser(username: String) = service.getDetailUser(username)

    fun getUserFollowers(username: String) = service.getUserFollowers(username)

    fun getUserFollowings(username: String) = service.getUserFollowings(username)
}