package com.timife.youverifytest.data.remote.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Data(
    @SerializedName("authorization_url")
    val authorizationUrl: String?,
    @SerializedName("access_code")
    val accessCode: String?,
    @SerializedName("reference")
    val reference: String?
)