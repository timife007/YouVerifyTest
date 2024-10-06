package com.timife.youverifytest.presentation.screens.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.timife.youverifytest.databinding.FragmentSignupBinding
import com.timife.youverifytest.navigation.Login
import com.timife.youverifytest.presentation.states.AuthUiState
import com.timife.youverifytest.presentation.utils.Utils.navOptions
import com.timife.youverifytest.presentation.utils.Utils.showSnackbar
import com.timife.youverifytest.presentation.utils.Validation.validateSignupCredentials
import com.timife.youverifytest.presentation.viewmodels.auth.CreateAccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CreateAccountFragment : Fragment() {
    private lateinit var signupBinding: FragmentSignupBinding
    private val viewModel: CreateAccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        signupBinding = FragmentSignupBinding.inflate(inflater, container, false)
        return signupBinding.root
    }

    private fun signup(email: String, password: String, confirmPassword: String) {
        validateSignupCredentials(
            email,
            password,
            confirmPassword,
            isEmailValid = { isEmailValid, message ->
                if (!isEmailValid) {
                    signupBinding.signupEmailInputLayout.error = message
                } else {
                    signupBinding.signupEmailInputLayout.error = null
                }
            },
            isPasswordValid = { isPasswordValid, message, isConfirmValid ->
                // Clears previous error messages
                signupBinding.apply {
                    signupPasswordInputLayout.error = null
                    confirmPasswordInputLayout.error = null
                }
                // Handle password and confirmation validation
                when {
                    !isPasswordValid && isConfirmValid -> signupBinding.signupPasswordInputLayout.error = message
                    isPasswordValid && !isConfirmValid -> signupBinding.confirmPasswordInputLayout.error = message
                }
            },
            allValidated = {
                signupBinding.signupProgress.visibility = View.VISIBLE
                createAccount(email, password)
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email = signupBinding.signupEmailInputText.text
        val password = signupBinding.signupPasswordInputText.text
        val confirmPassword = signupBinding.confirmPasswordInputText.text

        signupBinding.createAcctLayout.setOnClickListener {
            signup(email.toString(), password.toString(), confirmPassword.toString())
        }
        signupBinding.loginText.setOnClickListener {
            findNavController().navigate(Login, navOptions)
        }
        lifecycleScope.launch {
            viewModel.authUiState.collectLatest { state ->
                when (state) {
                    // Handle different states of authentication
                    is AuthUiState.Success -> {
                        // Sign in success, update UI with the signed-in user's information
                        findNavController().navigate(Login, navOptions)
                        showSnackbar(
                            signupBinding.root,
                            state.message,
                        )
                        signupBinding.signupProgress.visibility = View.GONE
                    }

                    is AuthUiState.Loading -> {
                        signupBinding.signupProgress.visibility = View.VISIBLE
                    }

                    is AuthUiState.Error -> {
                        showSnackbar(
                            signupBinding.root,
                            state.message,
                        )
                        signupBinding.signupProgress.visibility = View.GONE
                    }
                    is AuthUiState.Idle -> {}
                }
            }
        }
    }


    private fun createAccount(email: String, password: String) {
        viewModel.createUserWithEmailAndPassword(email, password)
    }
}