package com.timife.youverifytest.domain.usecases

import com.timife.youverifytest.domain.repositories.CartRepository
import javax.inject.Inject

class GetTotalPriceUC @Inject constructor(
    private val cartRepository: CartRepository
){
    operator fun invoke() = cartRepository.totalPrice
}