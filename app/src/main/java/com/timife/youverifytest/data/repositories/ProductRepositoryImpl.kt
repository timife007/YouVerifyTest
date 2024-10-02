package com.timife.youverifytest.data.repositories

import com.timife.youverifytest.data.local.db.ProductDb
import com.timife.youverifytest.data.mappers.toCategory
import com.timife.youverifytest.data.mappers.toCategoryEntity
import com.timife.youverifytest.data.mappers.toProduct
import com.timife.youverifytest.data.mappers.toProductEntity
import com.timife.youverifytest.data.remote.ApiService
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
    private val db: ProductDb
) : ProductRepository {
    override fun getCategories(): Flow<Resource<List<String>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = apiService.getAllCategories()

                if (response.isSuccessful) {
                    response.body()?.let { categories ->
                        categories.map {
                            db.productDao.insertCategory(it.toCategoryEntity())
                        }
                    }
                }
                val categories = db.productDao.getAllCategories().map {
                    it.toCategory()
                }
                emit(Resource.Success(categories))
            } catch (e: Exception) {
                emit(Resource.Error(e.message))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getProducts(): Flow<Resource<List<Product>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = apiService.getAllProducts()
                if (response.isSuccessful) {
                    response.body()?.let { products ->
                        db.productDao.insertProducts(products.map { it.toProductEntity() })
                    }
                }
                val products = db.productDao.getAllProducts().map { item ->
                    item.toProduct()
                }

                if(products.isEmpty()){
                    emit(Resource.Error("No products found"))
                }
                emit(Resource.Success(products))
            } catch (e: Exception) {
                emit(Resource.Error(e.message))
            }
        }.flowOn(Dispatchers.IO)
    }
}