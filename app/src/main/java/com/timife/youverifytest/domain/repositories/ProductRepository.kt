package com.timife.youverifytest.domain.repositories

import com.timife.youverifytest.domain.Resource
import com.timife.youverifytest.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository{

    fun getProducts(): Flow<Resource<List<Product>>>

    fun getProductById(productId: Int): Flow<Resource<Product>>
}