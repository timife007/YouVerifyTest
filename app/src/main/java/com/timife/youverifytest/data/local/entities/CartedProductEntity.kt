package com.timife.youverifytest.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carted_product")
data class CartedProductEntity(
    @PrimaryKey
    val id:Int,
    val productId:Int,
    val title:String,
    val price:Double,
    val description:String,
    val category:String,
    val image:String,
    val count:Int,
)