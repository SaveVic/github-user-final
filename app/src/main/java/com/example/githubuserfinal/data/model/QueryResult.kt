package com.example.githubuserfinal.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class QueryResult(
    @SerializedName("total_count") val total_count: Int = 0,
    @SerializedName("items") val items: List<QueryUser> = listOf(),
)


data class QueryUser(
    @SerializedName("login") val login: String = "",
    @SerializedName("id") val id: Int = 0,
    @SerializedName("avatar_url") val avatar_url: String = "",
): Serializable


