package com.timife.youverifytest.domain.model

import com.timife.youverifytest.data.remote.models.Data

data class PaymentAccess(
    val status: Boolean?,
    val message: String?,
    val data: Data?
)
