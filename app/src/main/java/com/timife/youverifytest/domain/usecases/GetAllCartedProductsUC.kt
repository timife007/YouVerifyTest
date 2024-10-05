package com.timife.youverifytest.domain.usecases

import com.timife.youverifytest.domain.repositories.CartRepository
import javax.inject.Inject

class GetAllCartedProductsUC @Inject constructor(
    private val cartRepository: CartRepository
){
    operator fun invoke() = cartRepository.cartedProducts
}