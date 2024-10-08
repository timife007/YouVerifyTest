package com.timife.youverifytest.data.remote.services

import com.timife.youverifytest.BuildConfig
import com.timife.youverifytest.data.remote.models.PaymentAccessDto
import com.timife.youverifytest.data.remote.models.requests.PaymentInitRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PaymentApiService {
    @Headers(
        "Authorization: Bearer ${BuildConfig.SECRET_KEY}",
        "Content-Type: application/json"
    )
    @POST("/transaction/initialize")
    suspend fun createOrder(
        @Body request: PaymentInitRequest
    ): Response<PaymentAccessDto>
}