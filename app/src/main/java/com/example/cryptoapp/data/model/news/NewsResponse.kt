package com.example.cryptoapp.data.model.news
import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("author")
    val author: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("updated_at")
    val updated: Long?,
    @SerializedName("news_site")
    val site: String?,
    @SerializedName("thumb_2x")
    val logo: String?
)
