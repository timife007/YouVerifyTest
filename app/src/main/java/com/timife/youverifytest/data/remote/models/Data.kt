package com.timife.youverifytest.data.remote.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Data(
    @SerializedName("authorization_url")
    val authorizationUrl: String?,
    @SerializedName("access_code")
    val accessCode: String?,
    @SerializedName("reference")
    val reference: String?
)