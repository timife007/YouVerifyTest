package com.timife.youverifytest.domain.repositories

import com.timife.youverifytest.domain.model.CartedProduct
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    val cartedProducts: Flow<List<CartedProduct>>

    val totalPrice: Flow<Double>

    suspend fun addToCart(productId: Int, count: Int)

    fun cartedProductQty(productId: Int?): Flow<Int>
}