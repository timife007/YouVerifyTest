package com.timife.youverifytest.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.timife.youverifytest.data.local.daos.CacheDao
import com.timife.youverifytest.data.local.daos.CartDao
import com.timife.youverifytest.data.local.entities.CartedProductEntity
import com.timife.youverifytest.data.local.entities.ProductEntity

@Database(entities = [ProductEntity::class, CartedProductEntity::class], version = 1, exportSchema = false)
abstract class ProductDb : RoomDatabase(){
    abstract val productDao: CacheDao
    abstract val cartDao: CartDao
}