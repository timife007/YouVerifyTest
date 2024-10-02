package com.timife.youverifytest.di

import android.content.Context
import androidx.room.Room
import com.timife.youverifytest.data.local.db.ProductDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocalModule{

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ProductDb{
        return Room.databaseBuilder(
            context,
            ProductDb::class.java,
            "product_db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideProductDao(db: ProductDb) = db.productDao

    @Singleton
    @Provides
    fun provideCartDao(db: ProductDb) = db.cartDao
}