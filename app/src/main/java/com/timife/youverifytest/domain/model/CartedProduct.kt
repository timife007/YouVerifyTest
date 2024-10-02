package com.timife.youverifytest.domain.model

data class CartedProduct(
    val id:Int,
    val productId:Int,
    val title:String,
    val price:Double,
    val description:String,
    val category:String,
    val image:String,
    val count:Int,
)