package com.timife.youverifytest.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.timife.youverifytest.domain.usecases.FilterProductByCategoryUC
import com.timife.youverifytest.domain.usecases.GetAllProductsUC
import com.timife.youverifytest.domain.usecases.SearchProductUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getAllProductsUC: GetAllProductsUC,
    private val filterProductByCategoryUC: FilterProductByCategoryUC,
    private val searchProductUC: SearchProductUC
): ViewModel() {
}