package com.timife.youverifytest.data.repositories

import com.timife.youverifytest.data.local.daos.CacheDao
import com.timife.youverifytest.data.mappers.toCategory
import com.timife.youverifytest.data.mappers.toCategoryEntity
import com.timife.youverifytest.data.mappers.toProduct
import com.timife.youverifytest.data.mappers.toProductEntity
import com.timife.youverifytest.data.remote.ApiService
import com.timife.youverifytest.data.utils.fetchAndCache
import com.timife.youverifytest.data.utils.loadFromDb
import com.timife.youverifytest.domain.Resource
import com.timife.youverifytest.domain.model.Product
import com.timife.youverifytest.domain.repositories.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dao: CacheDao
) : ProductRepository {
    override fun getCategories(): Flow<Resource<List<String>>> {
        return flow {
            emit(Resource.Loading()) // Emit loading state
            try {
                val response = apiService.getAllCategories() // Fetch categories from API

                if (response.isSuccessful) {
                    response.body()?.let { categories ->
                        // Insert each category into the database
                        categories.map {
                            dao.insertCategory(it.toCategoryEntity())
                        }
                    }
                }

                // Get categories from local database
                val categories = dao.getAllCategories().map {
                    it.toCategory()
                }

                if (categories.isEmpty()) {
                    emit(Resource.Error("No categories found")) // Emit error if no categories
                } else {
                    emit(Resource.Success(categories)) // Emit success with category list
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message)) // Emit error on exception
            }
        }.flowOn(Dispatchers.IO) // Execute on IO thread
    }

    override fun getProducts(): Flow<Resource<List<Product>>> {
        return flow {
            emit(Resource.Loading()) // Emit loading state
            try {
                val response = apiService.getAllProducts() // Fetch products from API

                if (response.isSuccessful) {
                    response.body()?.let { products ->
                        // Insert products into the database
                        dao.insertProducts(products.map { it.toProductEntity() })
                    }
                }

                // Get products from local database
                val products = dao.getAllProducts().map { item ->
                    item.toProduct()
                }

                if (products.isEmpty()) {
                    emit(Resource.Error("No products found")) // Emit error if no products
                } else {
                    emit(Resource.Success(products)) // Emit success with product list
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message)) // Emit error on exception
            }
        }.flowOn(Dispatchers.IO) // Execute on IO thread
    }

    override fun getProductsByCategory(category: String): Flow<Resource<List<Product>>> {
        return flow {
            emit(Resource.Loading()) // Emit loading state
            try {
                // Get products by category from the local database
                val products = dao.getProductsByCategory(category).map { item ->
                    item.toProduct()
                }

                if (products.isEmpty()) {
                    emit(Resource.Error("No products found")) // Emit error if no products
                } else {
                    emit(Resource.Success(products)) // Emit success with product list
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message)) // Emit error on exception
            }
        }.flowOn(Dispatchers.IO) // Execute on IO thread
    }

    override fun searchProducts(query: String): Flow<Resource<List<Product>>> {
        return flow {
            emit(Resource.Loading()) // Emit loading state
            try {
                // Search products in the local database by query
                val products = dao.searchProducts(query).map { item ->
                    item.toProduct()
                }

                if (products.isEmpty()) {
                    emit(Resource.Error("No products found")) // Emit error if no products
                } else {
                    emit(Resource.Success(products)) // Emit success with product list
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message)) // Emit error on exception
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
                    emit(Resource.Error("Product not found")) // Emit error if no product found
                } else {
                    emit(Resource.Success(product)) // Emit success with the product
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message)) // Emit error on exception
            }
        }.flowOn(Dispatchers.IO) // Execute on IO thread
    }
}