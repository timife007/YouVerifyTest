package com.timife.youverifytest.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.timife.youverifytest.domain.usecases.AddProductToCartUC
import com.timife.youverifytest.domain.usecases.GetSingleProductUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getSingleProductUC: GetSingleProductUC,
    private val addProductToCartUC: AddProductToCartUC
): ViewModel() {
}