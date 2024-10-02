package com.timife.youverifytest.domain.repositories

import com.timife.youverifytest.domain.Resource
import com.timife.youverifytest.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository{


    fun getCategories(): Flow<Resource<List<String>>>

    fun getProducts(): Flow<Resource<List<Product>>>

    fun getProductsByCategory(category: String): Flow<Resource<List<Product>>>

    fun searchProducts(query: String): Flow<Resource<List<Product>>>

    fun getProductById(productId: Int): Flow<Resource<Product>>
}