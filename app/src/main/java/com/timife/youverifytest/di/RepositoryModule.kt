package com.timife.youverifytest.di

import com.timife.youverifytest.data.repositories.CartRepositoryImpl
import com.timife.youverifytest.data.repositories.ProductRepositoryImpl
import com.timife.youverifytest.domain.repositories.CartRepository
import com.timife.youverifytest.domain.repositories.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ): CartRepository



}