package com.timife.youverifytest.data.repositories

import com.timife.youverifytest.data.local.daos.CacheDao
import com.timife.youverifytest.data.mappers.toProduct
import com.timife.youverifytest.data.mappers.toProductEntity
import com.timife.youverifytest.data.remote.services.ProductsApiService
import com.timife.youverifytest.domain.Resource
import com.timife.youverifytest.domain.model.Product
import com.timife.youverifytest.domain.repositories.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productsApiService: ProductsApiService,
    private val dao: CacheDao
) : ProductRepository {

    override fun getProducts(): Flow<Resource<List<Product>>> {
        return flow {
            emit(Resource.Loading())
            try {
                // Fetch products from API initially
                val response = productsApiService.getAllProducts()

                if (response.isSuccessful) {
                    response.body()?.let { products ->
                        //Insert products into the database if body is not null.
                        dao.insertProducts(products.map { it.toProductEntity() })
                    }
                }
            } catch (e: Exception) {
                // emit error on exception
                emit(Resource.Error(e.message))
            }
            // Get products from local database
            val products = dao.getAllProducts().map { item ->
                item.toProduct()
            }
            if (products.isEmpty()) {
                emit(Resource.Error("No products found"))
            } else {
                emit(Resource.Success(products)) // Emit success with product list
            }
        }.flowOn(Dispatchers.IO) // Execute on IO thread
    }


    override fun getProductById(productId: Int): Flow<Resource<Product>> {
        return flow {
            emit(Resource.Loading()) // Emit loading state
            try {
                // Get product by ID from local database
                val product = dao.getProductById(productId)?.toProduct()

                if (product == null) {
                    emit(Resource.Error("Product not found"))
                } else {
                    emit(Resource.Success(product))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message))
            }
        }.flowOn(Dispatchers.IO)
    }
}