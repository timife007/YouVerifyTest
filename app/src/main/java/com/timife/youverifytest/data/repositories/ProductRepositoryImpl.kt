package com.timife.youverifytest.data.repositories

import com.timife.youverifytest.data.local.daos.CacheDao
import com.timife.youverifytest.data.local.db.ProductDb
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
        return fetchAndCache(
            apiCall = { apiService.getAllCategories() },
            cacheCall = { dao.getAllCategories().map { it.toCategory() } },
            insertInDb = { categories ->
                categories.map {
                    dao.insertCategory(it.toCategoryEntity())
                }
            },
            mapToResult = { it }
        )
    }

    override fun getProducts(): Flow<Resource<List<Product>>> {
        return fetchAndCache(
            apiCall = { apiService.getAllProducts() },
            cacheCall = { dao.getAllProducts().map { it.toProduct() } },
            insertInDb = { products ->
                dao.insertProducts(products.map { it.toProductEntity() })
            },
            mapToResult = { it }
        )
    }

    override fun getProductsByCategory(category: String): Flow<Resource<List<Product>>> {
        return loadFromDb(
            dbCall = { dao.getProductsByCategory(category).map { it.toProduct() } },
            mapToResult = { it }
        )
    }

    override fun searchProducts(query: String): Flow<Resource<List<Product>>> {
        return loadFromDb(
            dbCall = { dao.searchProducts(query).map { it.toProduct() } },
            mapToResult = { it }
        )
    }

    override fun getProductById(productId: Int): Flow<Resource<Product>> {
        return flow {
            emit(Resource.Loading())
            try {
                // Get product by ID from local database
                val product = dao.getProductById(productId)?.toProduct()
                if (product == null) {
                    emit(Resource.Error("Product not found"))
                }
                emit(Resource.Success(product))
            } catch (e: Exception) {
                emit(Resource.Error(e.message))
            }
        }.flowOn(Dispatchers.IO)
    }


}