package com.timife.youverifytest.presentation.states

import com.timife.youverifytest.domain.model.Product

sealed class ProductUiState{
    data object Loading: ProductUiState()
    data class Success(val products: List<Product>): ProductUiState()
    data class Error(val error: String): ProductUiState()
}