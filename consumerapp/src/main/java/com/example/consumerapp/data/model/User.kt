package com.example.consumerapp.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("login") val login: String = "",
    @SerializedName("id") val id: Int = 0,
    @SerializedName("avatar_url") val avatar_url: String = "",
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("followers") val followers: Int = 0,
    @SerializedName("following") val following: Int = 0,
)
