package com.timife.youverifytest.domain.usecases

import com.timife.youverifytest.domain.repositories.CartRepository
import javax.inject.Inject

class AddProductToCartUC @Inject constructor(
    private val cartRepository: CartRepository
){
    suspend operator fun invoke(productId:Int,count: Int) {
        cartRepository.addToCart(productId, count)
    }
}