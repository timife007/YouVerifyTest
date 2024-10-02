package com.timife.youverifytest.data.repositories

import com.timife.youverifytest.data.local.daos.CartDao
import com.timife.youverifytest.data.mappers.toCartedProduct
import com.timife.youverifytest.data.mappers.toCartedProductEntity
import com.timife.youverifytest.domain.model.CartedProduct
import com.timife.youverifytest.domain.repositories.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
): CartRepository {

    // Retrieves all carted products, converting each entity to a CartedProduct object
    override val cartedProducts = cartDao.getCartItems().map {
        it.map { item ->
            item.toCartedProduct()
        }
    }.flowOn(Dispatchers.IO)

    // Retrieves the total price of all items in the cart
    override val totalPrice: Flow<Double>
        get() = cartDao.getTotalPrice().flowOn(Dispatchers.IO)

    // Adds an item to the cart, or removes it if count is 0
    override suspend fun addToCart(cartItem: CartedProduct) {
        withContext(Dispatchers.IO){
            if(cartItem.count == 0){
                cartDao.deleteCartItem(cartItem.toCartedProductEntity())
                return@withContext
            }
            cartDao.insertCartItem(cartItem.toCartedProductEntity())
        }
    }
}