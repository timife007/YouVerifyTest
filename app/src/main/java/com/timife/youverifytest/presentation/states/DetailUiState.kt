package com.timife.youverifytest.presentation.states

import com.timife.youverifytest.domain.model.Product

sealed class DetailUiState {
    data object Loading: DetailUiState()
    data class Success(val data: Product):DetailUiState()
    data class Error(val error:String): DetailUiState()
}