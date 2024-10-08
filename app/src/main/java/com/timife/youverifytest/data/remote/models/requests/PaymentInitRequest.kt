package com.timife.youverifytest.data.remote.models.requests


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PaymentInitRequest(
    @SerializedName("email")
    val email: String?,
    @SerializedName("amount")
    val amount: String?
)