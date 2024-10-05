package com.timife.youverifytest.presentation.viewmodels


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timife.youverifytest.domain.Resource
import com.timife.youverifytest.domain.usecases.AddProductToCartUC
import com.timife.youverifytest.domain.usecases.GetCartedProductQtyUC
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
    private val cartedProductQtyUC: GetCartedProductQtyUC,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _detailState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val detailUiState: StateFlow<DetailUiState> = _detailState

    private val _productId = MutableStateFlow<Int?>(null)
    val productId : StateFlow<Int?> = _productId

    private val _quantity = MutableStateFlow(0)
    val quantity : StateFlow<Int> = _quantity

    init {
        savedStateHandle.getStateFlow("productId",- 1).let {
            getProduct(it.value)
            _productId.value = it.value
            updateCount()
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

    /**
     * Updates the qty of the product in the cart based on the productId,
     * anytime there's a change
     */
    private fun updateCount(){
        viewModelScope.launch {
            cartedProductQtyUC(productId.value).collectLatest{
                _quantity.value = it
            }
        }
    }

    //Either reduce quantity or increase quantity of item with productId in cart
    fun updateItemInCart(count:Int) {
        viewModelScope.launch {
            productId.value?.let {
                addProductToCartUC(it, count)
                updateCount()
            }
        }
    }

}