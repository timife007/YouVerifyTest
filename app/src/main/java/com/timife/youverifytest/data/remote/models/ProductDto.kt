package com.timife.youverifytest.data.remote.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ProductDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("category")
    val category: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("ratingDto")
    val ratingDto: RatingDto?
)