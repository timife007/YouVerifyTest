package com.timife.youverifytest.presentation.viewmodels.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timife.youverifytest.domain.Resource
import com.timife.youverifytest.domain.usecases.GetAllProductsUC
import com.timife.youverifytest.presentation.states.ProductUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getAllProductsUC: GetAllProductsUC,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState: StateFlow<ProductUiState> = _uiState



    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            getAllProductsUC().collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _uiState.value = ProductUiState.Loading
                    }

                    is Resource.Error -> {
                        _uiState.value =
                            ProductUiState.Error(it.message ?: "Unknown error occurred")
                    }

                    is Resource.Success -> {
                        if (it.data != null) {
                            _uiState.value =
                                ProductUiState.Success(it.data)
                        }else{
                            _uiState.value = ProductUiState.Error("No item found")
                        }
                    }
                }
            }
        }
    }
}