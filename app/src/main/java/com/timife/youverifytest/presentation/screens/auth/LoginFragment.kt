package com.timife.youverifytest.presentation.screens.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.timife.youverifytest.databinding.FragmentLoginBinding
import com.timife.youverifytest.navigation.Signup
import com.timife.youverifytest.presentation.MainActivity
import com.timife.youverifytest.presentation.states.LoginUiState
import com.timife.youverifytest.presentation.utils.Utils.navOptions
import com.timife.youverifytest.presentation.utils.Utils.showSnackbar
import com.timife.youverifytest.presentation.utils.Validation.validateLoginCredentials
import com.timife.youverifytest.presentation.viewmodels.auth.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var loginBinding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return loginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email = loginBinding.loginEmailInputText.text
        val password = loginBinding.loginPasswordInputText.text

        loginBinding.loginLayout.setOnClickListener {
            login(email.toString(), password.toString())
        }

        loginBinding.createAcctText.setOnClickListener {
            findNavController().navigate(Signup, navOptions)
        }

        lifecycleScope.launch {
            viewModel.loginUiState.collect { state ->
                when (state) {
                    is LoginUiState.Loading -> {
                        loginBinding.loginProgress.visibility = View.VISIBLE
                    }
                    is LoginUiState.Success -> {
                        loginBinding.loginProgress.visibility = View.GONE
                        showSnackbar(loginBinding.root, state.message)
                        sharedPreferences.edit().putBoolean("is_logged_in", true).apply()
                        navigateToMainActivity()
                    }
                    is LoginUiState.Error -> {
                        showSnackbar(loginBinding.root, state.message)
                        loginBinding.loginProgress.visibility = View.GONE
                    }
                    else -> {}
                }
            }
        }
    }



    private fun login(email: String, password: String) {
        validateLoginCredentials(email, password, isPasswordValid = { isValid, message ->
            if (!isValid) {
                loginBinding.loginPasswordInputLayout.error = message
            } else {
                loginBinding.loginPasswordInputLayout.error = null
            }
        }, isEmailValid = { isValid, message ->
            if (!isValid) {
                loginBinding.loginEmailInputLayout.error = message
            } else {
                loginBinding.loginEmailInputLayout.error = null
            }
        }, allValidated = {
            loginBinding.loginProgress.visibility = View.VISIBLE
            viewModel.signInWithUsernameAndPassword(email, password)
        })
    }

    // Navigate to MainActivity after successful login
    private fun navigateToMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}