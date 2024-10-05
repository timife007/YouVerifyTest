package com.timife.youverifytest.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timife.youverifytest.domain.Resource
import com.timife.youverifytest.domain.model.Product
import com.timife.youverifytest.domain.usecases.FilterProductByCategoryUC
import com.timife.youverifytest.domain.usecases.GetAllCategoriesUC
import com.timife.youverifytest.domain.usecases.GetAllProductsUC
import com.timife.youverifytest.domain.usecases.SearchProductUC
import com.timife.youverifytest.presentation.states.ProductUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getAllProductsUC: GetAllProductsUC,
    private val filterProductByCategoryUC: FilterProductByCategoryUC,
    private val searchProductUC: SearchProductUC,
    private val getALlCategoryUC: GetAllCategoriesUC
) : ViewModel() {


    private val _uiState = mutableStateOf<ProductUiState>(ProductUiState.Loading)
    val uiState: State<ProductUiState> = _uiState

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
                                ProductUiState.Success(it.data, it.data.distinctBy { x ->
                                    x.category
                                }.map { y ->
                                    y.category
                                })

                        }else{
                            _uiState.value = ProductUiState.Error("No item found")
                        }
                    }
                }
            }
        }
    }

}