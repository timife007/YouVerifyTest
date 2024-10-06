package com.timife.youverifytest.presentation.screens.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.timife.youverifytest.R
import com.timife.youverifytest.databinding.FragmentLoginBinding
import com.timife.youverifytest.navigation.ProductList
import com.timife.youverifytest.navigation.Signup
import com.timife.youverifytest.presentation.MainActivity
import com.timife.youverifytest.presentation.utils.Utils.navOptions
import com.timife.youverifytest.presentation.utils.Utils.showSnackbar
import com.timife.youverifytest.presentation.utils.Validation.areLoginCredentialsValid

class LoginFragment : Fragment() {

    private lateinit var loginBinding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
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
        sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val email = loginBinding.loginEmailInputText.text
        val password = loginBinding.loginPasswordInputText.text
        loginBinding.loginLayout.setOnClickListener {
            login(email.toString(), password.toString())
        }

        loginBinding.createAcctText.setOnClickListener {
            findNavController().navigate(Signup, navOptions)
        }
        findNavController().popBackStack()
    }


    private fun login(email: String, password: String) {
        areLoginCredentialsValid(email, password, isPasswordValid = { isValid, message ->
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
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        showSnackbar(
                            loginBinding.root,
                            getString(R.string.login_successful),
                            Snackbar.LENGTH_LONG,
                        )
                        sharedPreferences.edit().putBoolean("is_logged_in", true).apply()
                        navigateToMainActivity()
                    } else {
                        // If sign in fails, display a message to the user.
                        showSnackbar(
                            loginBinding.root,
                            task.exception?.message ?: getString(R.string.unknown_error_occurred),
                            Snackbar.LENGTH_LONG,
                        )
                    }
                    loginBinding.loginProgress.visibility = View.GONE
                }
        })
    }


    private fun navigateToMainActivity() {
        // Navigate to MainActivity after successful login
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}