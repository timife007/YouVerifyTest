package com.timife.youverifytest.domain.repositories

import com.timife.youverifytest.domain.model.CartedProduct
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    val cartedProducts: Flow<List<CartedProduct>>

    suspend fun addToCart(cartItem: CartedProduct)
}