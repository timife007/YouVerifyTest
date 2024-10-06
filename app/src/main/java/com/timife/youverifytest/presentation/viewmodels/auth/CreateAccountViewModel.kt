package com.timife.youverifytest.presentation.viewmodels.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.timife.youverifytest.presentation.states.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val auth:FirebaseAuth
): ViewModel() {

    private val _authUiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val authUiState:StateFlow<AuthUiState> = _authUiState

    // Function to create a user with email and password
    fun createUserWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading
            try {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (auth.currentUser != null) {
                            auth.currentUser!!.sendEmailVerification()
                            _authUiState.value = AuthUiState.Success(isSuccess = true, message = "Account created successfully, Please check your email to verify!")
                        }else{
                            _authUiState.value = AuthUiState.Error(message = "Unable to create account")
                        }
                    } else {
                        _authUiState.value = AuthUiState.Error(task.exception?.message ?: "Unknown Error Occurred, please try again")
                    }
                }
            } catch (e: FirebaseAuthException) {
                _authUiState.value = AuthUiState.Error("Unknown error, please check internet")
            }
        }
    }
}