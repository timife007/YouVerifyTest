package com.timife.youverifytest.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.timife.youverifytest.data.local.entities.CartedProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao{

    @Query("SELECT * FROM carted_product")
    fun getCartItems(): Flow<List<CartedProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartedProductEntity)

    @Query("SELECT SUM(price * quantity) FROM carted_product")
    fun getTotalPrice(): Flow<Double>

    @Query("DELETE FROM carted_product WHERE productId = :productId")
    suspend fun deleteCartItem(productId: Int)

    @Query("SELECT quantity FROM carted_product WHERE productId = :id")
    suspend fun getCartedItemCount(id:Int): Int?

    @Query("SELECT * FROM carted_product WHERE productId = :id")
    suspend fun getSingleCartedProduct(id:Int): CartedProductEntity?

    @Query("DELETE FROM carted_product")
    suspend fun clearCart()
}