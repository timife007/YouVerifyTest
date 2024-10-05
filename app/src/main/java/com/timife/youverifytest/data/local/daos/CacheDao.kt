package com.timife.youverifytest.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.timife.youverifytest.data.local.entities.ProductEntity

@Dao
interface CacheDao {

    @Query("SELECT * FROM product")
    suspend fun getAllProducts(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Query("DELETE FROM product")
    suspend fun clearAllProducts()


    @Query("SELECT * FROM product WHERE category LIKE '%' || :category || '%'")
    suspend fun getProductsByCategory(category: String): List<ProductEntity>

    @Query("SELECT * FROM product WHERE id = :productId")
    suspend fun getProductById(productId: Int): ProductEntity?

}