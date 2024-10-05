package com.timife.youverifytest.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.timife.youverifytest.domain.model.Rating

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey
    val id:Int,
    val title:String,
    val price:Double,
    val description:String,
    val category:String,
    val image:String,
    val rating: Double,
    val review:Int
)