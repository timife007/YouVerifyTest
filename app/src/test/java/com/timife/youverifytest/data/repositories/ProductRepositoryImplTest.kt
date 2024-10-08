package com.timife.youverifytest.data.repositories

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.timife.youverifytest.data.db.FakeProductDao
import com.timife.youverifytest.data.db.fakeApiResponse
import com.timife.youverifytest.data.db.productList
import com.timife.youverifytest.data.db.productsDto
import com.timife.youverifytest.data.mappers.toProduct
import com.timife.youverifytest.data.mappers.toProductEntity
import com.timife.youverifytest.data.remote.services.ProductsApiService
import com.timife.youverifytest.domain.Resource
import com.timife.youverifytest.domain.repositories.ProductRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ProductRepositoryImplTest {

    private lateinit var repository:ProductRepository
    private lateinit var dao: FakeProductDao
    private lateinit var api: ProductsApiService

    @Before
    fun setUp() {
        dao = FakeProductDao()
        api = mockk(relaxed = true){
            coEvery {
                getAllProducts()
            } returns fakeApiResponse
        }
        repository = ProductRepositoryImpl(
            api,
            dao
        )
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getProducts() = runTest {
        repository.getProducts().test {
            val loading = awaitItem()
            assertThat((loading as Resource.Loading).isLoading).isTrue()
            val items = awaitItem()
            assertThat(items is Resource.Success).isTrue()
            val products = dao.getAllProducts()
            assertThat(products).isNotEmpty()
            assertThat(products).isEqualTo(productsDto.map { it.toProductEntity() })
            awaitComplete()
        }
    }

    @Test
    fun `check if correct id returns the right flow`() = runTest{
        dao.insertProducts(productList)
        repository.getProductById(1).test {
            val loading = awaitItem()
            assertThat((loading as Resource.Loading).isLoading).isTrue()
            val items = awaitItem()
            assertThat(items is Resource.Success).isTrue()
            assertThat(items).isNotNull()
            awaitComplete()
        }

    }

    @Test
    fun `check if incorrect id found returns the right flow`() = runTest{
        dao.insertProducts(productList)
        repository.getProductById(11).test {
            val loading = awaitItem()
            assertThat((loading as Resource.Loading).isLoading).isTrue()
            val items = awaitItem()
            assertThat(items is Resource.Error).isTrue()
            awaitComplete()
        }

    }
}