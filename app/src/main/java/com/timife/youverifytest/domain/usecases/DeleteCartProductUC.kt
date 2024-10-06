package com.timife.youverifytest.domain.usecases

import com.timife.youverifytest.domain.repositories.CartRepository
import javax.inject.Inject

class DeleteCartProductUC @Inject constructor(
    private val repository: CartRepository
) {

    suspend operator fun invoke(productId: Int){
        repository.removeFromCart(productId)
    }

}