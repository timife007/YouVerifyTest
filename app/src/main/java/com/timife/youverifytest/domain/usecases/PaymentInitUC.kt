package com.timife.youverifytest.domain.usecases

import com.timife.youverifytest.domain.repositories.CartRepository
import javax.inject.Inject

class PaymentInitUC @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke(price: Double) =
        repository.initializePayment(price)

}