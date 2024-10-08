package com.timife.youverifytest.data.db

import com.timife.youverifytest.data.local.daos.CacheDao
import com.timife.youverifytest.data.local.entities.ProductEntity
import com.timife.youverifytest.data.remote.models.ProductDto
import com.timife.youverifytest.data.remote.models.RatingDto
import retrofit2.Response

class FakeProductDao(): CacheDao {

    private var db = emptyList<ProductEntity>()

    override suspend fun getAllProducts(): List<ProductEntity> {
        return db
    }

    override suspend fun insertProducts(products: List<ProductEntity>) {
        db = db + products
    }

    override suspend fun clearAllProducts() {
        db = emptyList()
    }


    override suspend fun getProductById(productId: Int): ProductEntity? {
        val item = db.find {
            it.id == productId
        }
        return item
    }
}

val productsDto  = listOf(
    ProductDto(
        id = 1,
        title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
        price = 109.95,
        description = "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
        category = "men's clothing",
        image = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
        ratingDto = RatingDto(rate = 3.9, count = 120)
    ),
    ProductDto(
        id = 2,
        title = "Mens Casual Premium Slim Fit T-Shirts",
        price = 22.3,
        description = "Slim-fitting style, contrast raglan long sleeve, three-button henley placket, light weight & soft fabric for breathable and comfortable wearing. And Solid stitched shirts with round neck made for durability and a great fit for casual fashion wear and diehard baseball fans. The Henley style round neckline includes a three-button placket.",
        category = "men's clothing",
        image = "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_.jpg",
        ratingDto = RatingDto(rate = 4.1, count = 259)
    ),
    ProductDto(
        id = 3,
        title = "Mens Cotton Jacket",
        price = 55.99,
        description = "Great outerwear jackets for Spring/Autumn/Winter, suitable for many occasions, such as working, hiking, camping, mountain/rock climbing, cycling, traveling or other outdoors.",
        category = "men's clothing",
        image = "https://fakestoreapi.com/img/71li-ujtlUL._AC_UX679_.jpg",
        ratingDto = RatingDto(rate = 4.7, count = 500)
    ))

val fakeApiResponse: Response<List<ProductDto>> = Response.success(
    productsDto
)

val productList = listOf(
    ProductEntity(
        id = 1,
        title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
        price = 109.95,
        description = "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
        category = "men's clothing",
        image = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
        rating = 3.9,
        review = 120
    ),
    ProductEntity(
        id = 2,
        title = "Mens Casual Premium Slim Fit T-Shirts",
        price = 22.3,
        description = "Slim-fitting style, contrast raglan long sleeve, three-button henley placket, light weight & soft fabric for breathable and comfortable wearing. And Solid stitched shirts with round neck made for durability and a great fit for casual fashion wear and diehard baseball fans. The Henley style round neckline includes a three-button placket.",
        category = "men's clothing",
        image = "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_.jpg",
        rating =4.1,
        review = 259
    ),
    ProductEntity(
        id = 3,
        title = "Mens Cotton Jacket",
        price = 55.99,
        description = "Great outerwear jackets for Spring/Autumn/Winter, suitable for many occasions, such as working, hiking, camping, mountain/rock climbing, cycling, traveling or other outdoors.",
        category = "men's clothing",
        image = "https://fakestoreapi.com/img/71li-ujtlUL._AC_UX679_.jpg",
        rating = 4.7,
        review = 500
    ),
    ProductEntity(
        id = 4,
        title = "Mens Casual Slim Fit",
        price = 15.99,
        description = "The color could be slightly different between on the screen and in practice.",
        category = "men's clothing",
        image = "https://fakestoreapi.com/img/71YXzeOuslL._AC_UY879_.jpg",
        rating = 2.1,
        review = 430
    ))