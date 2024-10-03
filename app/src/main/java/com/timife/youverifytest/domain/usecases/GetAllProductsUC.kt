package com.timife.youverifytest.domain.usecases

import com.timife.youverifytest.domain.repositories.ProductRepository
import javax.inject.Inject

class GetAllProductsUC @Inject constructor(
    private val productRepository: ProductRepository
){
    operator fun invoke() = productRepository.getProducts()
}