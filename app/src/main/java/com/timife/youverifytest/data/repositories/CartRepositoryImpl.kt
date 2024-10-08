package com.timife.youverifytest.data.repositories

import android.util.Log
import com.timife.youverifytest.data.local.daos.CacheDao
import com.timife.youverifytest.data.local.daos.CartDao
import com.timife.youverifytest.data.mappers.toCartedProduct
import com.timife.youverifytest.data.mappers.toCartedProductEntity
import com.timife.youverifytest.data.mappers.toPaymentAccess
import com.timife.youverifytest.data.remote.models.requests.PaymentInitRequest
import com.timife.youverifytest.data.remote.services.PaymentApiService
import com.timife.youverifytest.domain.Resource
import com.timife.youverifytest.domain.model.PaymentAccess
import com.timife.youverifytest.domain.repositories.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao,
    private val cacheDao: CacheDao,
    private val paystackApiService: PaymentApiService
) : CartRepository {


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
    override suspend fun addToCart(productId: Int, count: Int) {
        withContext(Dispatchers.IO) {
            // Get the product and cart item from the database
            val product = cacheDao.getProductById(productId)
            val cartedItem = cartDao.getSingleCartedProduct(productId)
            /**
             * If the count is 0, remove the item from the cart, ELSE
             * If the item is not in the cart, map it to the entity and
             * add it to the db; if present already, update it with the new quantity
             */
            if (count == 0) {
                cartDao.deleteCartItem(productId)
            } else {
                if (cartedItem == null) {
                    val newCartItem = product?.toCartedProductEntity(count)
                    cartDao.insertCartItem(newCartItem!!)
                } else {
                    val oldCartItem = cartedItem.copy(quantity = count)
                    cartDao.insertCartItem(oldCartItem)
                }
            }

        }
    }

    override fun cartedProductQty(productId: Int?): Flow<Int> = flow {
        productId?.let {
            /**
             * check if product is in cart, if not, then return 0
             * else, return the quantity
             */
            val qty = cartDao.getCartedItemCount(productId)
            if (qty == null) {
                emit(0)
            } else {
                emit(qty)
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun removeFromCart(productId: Int) {
        withContext(Dispatchers.IO){
            try {
                cartDao.deleteCartItem(productId)
            }catch (e:Exception){
                Log.e("removeFromCart", e.message.toString())
            }
        }
    }

    override fun initializePayment(amount: Double) : Flow<Resource<PaymentAccess>> = flow{

        val request = PaymentInitRequest(
            email = "timife007@gmail.com",
            amount = amount.toInt().toString()
        )
        emit(Resource.Loading())
        try {
            val response = paystackApiService.createOrder(request)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it.toPaymentAccess()))
                }
            }
        }catch (e:Exception){
            emit(Resource.Error(e.message))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun clearCart() {
        withContext(Dispatchers.IO){
            try {
                cartDao.clearCart()
            }catch (e:Exception){

            }
        }
    }
}