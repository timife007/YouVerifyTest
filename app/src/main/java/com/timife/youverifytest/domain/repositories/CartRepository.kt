package com.timife.youverifytest.domain.repositories

import com.timife.youverifytest.domain.Resource
import com.timife.youverifytest.domain.model.CartedProduct
import com.timife.youverifytest.domain.model.PaymentAccess
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    val cartedProducts: Flow<List<CartedProduct>>

    val totalPrice: Flow<Double>

    suspend fun addToCart(productId: Int, count: Int)

    fun cartedProductQty(productId: Int?): Flow<Int>

    suspend fun removeFromCart(productId: Int)

    fun initializePayment(amount: Double):Flow<Resource<PaymentAccess>>

    suspend fun clearCart()
}