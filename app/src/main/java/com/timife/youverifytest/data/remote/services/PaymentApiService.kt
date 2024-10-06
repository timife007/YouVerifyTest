package com.timife.youverifytest.data.remote.services

import com.timife.youverifytest.data.remote.models.PaymentAccessDto
import com.timife.youverifytest.data.remote.models.requests.PaymentInitRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PaymentApiService {
    @Headers(
        "Authorization: Bearer sk_test_d760d59248f6ad8cfe3b7c61f91f6e32a6d8a0a7",
        "Content-Type: application/json"
    )
    @POST("/transaction/initialize")
    suspend fun createOrder(
        @Body request: PaymentInitRequest
    ): Response<PaymentAccessDto>
}