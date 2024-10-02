package com.timife.youverifytest.data.mappers

import com.timife.youverifytest.data.local.entities.CartedProductEntity
import com.timife.youverifytest.data.local.entities.CategoryEntity
import com.timife.youverifytest.data.local.entities.ProductEntity
import com.timife.youverifytest.data.remote.models.ProductDto
import com.timife.youverifytest.data.remote.models.RatingDto
import com.timife.youverifytest.domain.model.CartedProduct
import com.timife.youverifytest.domain.model.Product
import com.timife.youverifytest.domain.model.Rating


fun ProductDto.toProductEntity(): ProductEntity {
    return ProductEntity(
        id = id ?: 0,
        title = title ?: "",
        price = price ?: 0.0,
        description = description ?: "",
        category = category ?: "",
        image = image ?: "",
        rating = ratingDto?.toRating() ?: Rating(0.0,0)
    )
}

fun RatingDto.toRating(): Rating{
    return Rating(
        rate = rate ?: 0.0,
        count = count ?: 0
    )
}

fun ProductEntity.toProduct():Product{
    return Product(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        image = image,
        rating = rating
    )
}

fun Product.toProductEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        image = image,
        rating = rating
    )
}

fun CartedProduct.toCartedProductEntity(): CartedProductEntity {
    return CartedProductEntity(
        id = id,
        productId = productId,
        title = title,
        price = price,
        description = description,
        category = category,
        image = image,
        count = count,
    )
}

fun CartedProductEntity.toCartedProduct():CartedProduct{
    return CartedProduct(
        id = id,
        productId = productId,
        title = title,
        price = price,
        description = description,
        category = category,
        image = image,
        count = count,
    )
}

fun String.toCategoryEntity():CategoryEntity{
    return CategoryEntity(
        name = this
    )
}

fun CategoryEntity.toCategory():String{
    return this.name
}