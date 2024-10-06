package com.timife.youverifytest.presentation.viewmodels.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.timife.youverifytest.presentation.states.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState


    fun signInWithUsernameAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                try {
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        // Check if user's email is verified
                        if (user?.isEmailVerified == true) {
                            _loginUiState.value =
                                LoginUiState.Success(isSuccess = true, message = "Login Successful")
                        } else {
                            _loginUiState.value =
                                LoginUiState.Error(message = "Unable to login, please verify your email")
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        _loginUiState.value = LoginUiState.Error(
                            message = task.exception?.message
                                ?: "Unknown Error Occurred, please try again"
                        )
                    }
                } catch (e: FirebaseAuthException) {
                    _loginUiState.value =
                        LoginUiState.Error(message = "Unknown error, check internet")
                }
            }
    }
}