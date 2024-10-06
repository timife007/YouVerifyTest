package com.timife.youverifytest.presentation.states



sealed class AuthUiState{
    data object Loading: AuthUiState()
    data object Idle:AuthUiState()
    data class Success(val isSuccess:Boolean, val message:String):AuthUiState()
    data class Error(val message:String):AuthUiState()
}