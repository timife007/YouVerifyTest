package com.timife.youverifytest.presentation.states

sealed class InitPaymentState{
    data class Success(val accessCode: String): InitPaymentState()
    data class Error(val error: String): InitPaymentState()
    data object Loading: InitPaymentState()
}

