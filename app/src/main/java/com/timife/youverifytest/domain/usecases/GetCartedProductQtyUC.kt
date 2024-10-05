package com.timife.youverifytest.domain.usecases

import com.timife.youverifytest.domain.repositories.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartedProductQtyUC @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke(id: Int?): Flow<Int> = cartRepository.cartedProductQty(id)
}