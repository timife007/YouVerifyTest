package com.timife.youverifytest.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
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

    @Query("SELECT SUM(price * count) FROM carted_product")
    fun getTotalPrice(): Flow<Double>

    @Delete
    suspend fun deleteCartItem(cartItem: CartedProductEntity)
}