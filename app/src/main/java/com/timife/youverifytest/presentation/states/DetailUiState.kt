package com.timife.youverifytest.presentation.states

import com.timife.youverifytest.domain.model.CartedProduct
import com.timife.youverifytest.domain.model.Product

sealed class DetailUiState {
    data object Loading: DetailUiState()
    data class Success(val data: Product, val cartCount: Int = 0):DetailUiState()
    data class Error(val error:String): DetailUiState()
}