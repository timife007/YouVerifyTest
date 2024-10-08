package com.timife.youverifytest.domain.usecases

import com.timife.youverifytest.domain.repositories.CartRepository
import javax.inject.Inject

class ClearCartUC @Inject constructor(
    private val repository: CartRepository) {
    suspend operator fun invoke() {
        repository.clearCart()
    }
}