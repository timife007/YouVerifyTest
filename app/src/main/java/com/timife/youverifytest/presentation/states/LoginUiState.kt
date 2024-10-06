package com.timife.youverifytest.presentation.states

sealed class LoginUiState{
    data object Idle:LoginUiState()
    data object Loading:LoginUiState()
    data class Success(val isSuccess:Boolean,val message:String):LoginUiState()
    data class Error(val message:String):LoginUiState()
}