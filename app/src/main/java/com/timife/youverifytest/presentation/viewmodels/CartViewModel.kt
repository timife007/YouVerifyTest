package com.timife.youverifytest.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.timife.youverifytest.domain.usecases.AddProductToCartUC
import com.timife.youverifytest.domain.usecases.GetAllCartedProductsUC
import com.timife.youverifytest.domain.usecases.GetTotalPriceUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getAllCartedProductsUC: GetAllCartedProductsUC,
    private val getTotalPriceUC: GetTotalPriceUC,
    private val addProductToCartUC: AddProductToCartUC
): ViewModel(){

}