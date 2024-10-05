package com.timife.youverifytest.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.createGraph
import androidx.navigation.fragment.fragment
import com.timife.youverifytest.presentation.screens.CartFragment
import com.timife.youverifytest.presentation.screens.ProductDetailsFragment
import com.timife.youverifytest.presentation.screens.ProductListFragment
import kotlinx.serialization.Serializable

@Serializable
object ProductList


@Serializable
object Cart

@Serializable
data class ProductDetails(
    val productId: Int
)


fun navigationGraph(navController: NavController):NavGraph{
    return navController.createGraph(startDestination = ProductList){
        fragment<ProductListFragment, ProductList>{
            label = "Discover Products"
        }
        fragment<ProductDetailsFragment, ProductDetails>{
            label = "Product Details"
        }
        fragment<CartFragment, Cart>{
            label = "Cart"
        }
    }
}

