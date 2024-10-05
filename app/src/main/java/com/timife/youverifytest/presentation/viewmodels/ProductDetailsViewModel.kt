package com.timife.youverifytest.presentation.viewmodels


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timife.youverifytest.domain.Resource
import com.timife.youverifytest.domain.model.CartedProduct
import com.timife.youverifytest.domain.usecases.AddProductToCartUC
import com.timife.youverifytest.domain.usecases.GetSingleProductUC
import com.timife.youverifytest.presentation.states.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getSingleProductUC: GetSingleProductUC,
    private val addProductToCartUC: AddProductToCartUC,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId = savedStateHandle.get<Int?>("productId")
    private val _detailState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val detailUiState: StateFlow<DetailUiState> = _detailState


    init {
        productId?.let {
            getProduct(it)
        }
    }

    private fun getProduct(id: Int) {
        viewModelScope.launch {
            getSingleProductUC(id).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _detailState.value = DetailUiState.Loading
                    }

                    is Resource.Error -> {
                        _detailState.value =
                            DetailUiState.Error(it.message ?: "Unknown error occurred")
                    }

                    is Resource.Success -> {
                        if (it.data != null) {
                            _detailState.value = DetailUiState.Success(it.data)
                        }
                    }
                }
            }
        }
    }

    suspend fun addToCart(cartItem: CartedProduct) {
        addProductToCartUC(cartItem)
    }
}