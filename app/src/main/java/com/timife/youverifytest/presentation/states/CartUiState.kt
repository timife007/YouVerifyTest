package com.timife.youverifytest.presentation.states

import com.timife.youverifytest.domain.model.CartedProduct

sealed class CartUiState {
    data object Loading: CartUiState()
    data class Success(val data: List<CartedProduct>, val totalPrice: Double): CartUiState()
    data class Error(val error:String): CartUiState()
}