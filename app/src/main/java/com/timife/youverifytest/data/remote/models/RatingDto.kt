package com.timife.youverifytest.data.remote.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RatingDto(
    @SerializedName("rate")
    val rate: Double?,
    @SerializedName("count")
    val count: Int?
)