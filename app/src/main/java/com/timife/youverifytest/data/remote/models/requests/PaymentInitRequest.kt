package com.timife.youverifytest.data.remote.models.requests


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class PaymentInitRequest(
    @SerializedName("email")
    val email: String?,
    @SerializedName("amount")
    val amount: String?
)