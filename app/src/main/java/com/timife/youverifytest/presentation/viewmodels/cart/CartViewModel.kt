package com.timife.youverifytest.presentation.viewmodels.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timife.youverifytest.domain.usecases.DeleteCartProductUC
import com.timife.youverifytest.domain.usecases.GetAllCartedProductsUC
import com.timife.youverifytest.domain.usecases.GetTotalPriceUC
import com.timife.youverifytest.presentation.states.CartUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    getAllCartedProductsUC: GetAllCartedProductsUC,
    getTotalPriceUC: GetTotalPriceUC,
    private val deleteCartProductUC: DeleteCartProductUC
): ViewModel(){

    private val cartedProducts = getAllCartedProductsUC()
    private val totalPrice = getTotalPriceUC()

    val cartUiState = combine(cartedProducts, totalPrice){products, price->
        if (products.isEmpty()){
            CartUiState.Error("Your cart is empty")
        }else{
            CartUiState.Success(products, price)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CartUiState.Loading)


    fun deleteItemFromCart(productId: Int){
        viewModelScope.launch {
            deleteCartProductUC(productId)
        }
    }
}