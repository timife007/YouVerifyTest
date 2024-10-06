package com.timife.youverifytest.presentation.viewmodels.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timife.youverifytest.domain.Resource
import com.timife.youverifytest.domain.usecases.ClearCartUC
import com.timife.youverifytest.domain.usecases.DeleteCartProductUC
import com.timife.youverifytest.domain.usecases.GetAllCartedProductsUC
import com.timife.youverifytest.domain.usecases.GetTotalPriceUC
import com.timife.youverifytest.domain.usecases.PaymentInitUC
import com.timife.youverifytest.presentation.states.CartUiState
import com.timife.youverifytest.presentation.states.InitPaymentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    getAllCartedProductsUC: GetAllCartedProductsUC,
    getTotalPriceUC: GetTotalPriceUC,
    private val paymentInitUC: PaymentInitUC,
    private val deleteCartProductUC: DeleteCartProductUC,
    private val clearCartUC: ClearCartUC
): ViewModel(){

    private val cartedProducts = getAllCartedProductsUC()
    val totalPrice = getTotalPriceUC()

    private val _initPaymentState: MutableStateFlow<InitPaymentState?> = MutableStateFlow(null)
    val initPaymentState = _initPaymentState.asStateFlow()


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

     fun paymentInit(price: Double){
        viewModelScope.launch {
            paymentInitUC(price).collectLatest {
                when(it){
                    is Resource.Loading -> {
                        _initPaymentState.value = InitPaymentState.Loading
                    }
                    is Resource.Success -> {
                        if(it.data?.data?.accessCode == null){
                            _initPaymentState.value = InitPaymentState.Error(it.message ?: "Unknown error occurred")
                        }else{
                            _initPaymentState.value = InitPaymentState.Success(it.data.data.accessCode)
                        }
                    }
                    is Resource.Error -> {
                        _initPaymentState.value = InitPaymentState.Error(it.message ?: "Unknown error occurred")
                    }
                }
            }
        }
    }

    fun clearCartDB() {
        viewModelScope.launch {
            clearCartUC()
            cartedProducts
        }
    }
}