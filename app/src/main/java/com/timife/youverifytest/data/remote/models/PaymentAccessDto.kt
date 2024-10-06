package com.timife.youverifytest.data.remote.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class PaymentAccessDto(
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: Data?
)