package com.timife.youverifytest.presentation.screens.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.timife.youverifytest.R
import com.timife.youverifytest.databinding.FragmentSignupBinding
import com.timife.youverifytest.navigation.Login
import com.timife.youverifytest.presentation.utils.Utils.navOptions
import com.timife.youverifytest.presentation.utils.Utils.showSnackbar
import com.timife.youverifytest.presentation.utils.Validation
import com.timife.youverifytest.presentation.utils.Validation.areSignupCredentialsValid
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignupFragment : Fragment() {

    private lateinit var signupBinding: FragmentSignupBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initialize firebase auth
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        signupBinding = FragmentSignupBinding.inflate(inflater, container, false)
        val email = signupBinding.signupEmailInputText.text
        val password = signupBinding.signupPasswordInputText.text
        val confirmPassword = signupBinding.confirmPasswordInputText.text

        signupBinding.createAcctLayout.setOnClickListener {
            signup(email.toString(), password.toString(), confirmPassword.toString())
        }
        signupBinding.loginText.setOnClickListener {
            findNavController().navigate(Login, navOptions)
        }
        findNavController().popBackStack()
        return signupBinding.root
    }

    private fun signup(email: String, password: String, confirmPassword: String) {
        areSignupCredentialsValid(
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
                if (!isPasswordValid && isConfirmValid) {
                    signupBinding.signupPasswordInputLayout.error = message
                } else if(isPasswordValid && !isConfirmValid){
                    signupBinding.apply {
                        signupPasswordInputLayout.error = null
                        confirmPasswordInputLayout.error = message
                    }
                }else {
                    signupBinding.apply {
                        signupPasswordInputLayout.error = null
                        confirmPasswordInputLayout.error = null
                    }
                }
            },
            allValidated = {
                signupBinding.signupProgress.visibility = View.VISIBLE
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            if (user != null) {
                                findNavController().navigate(Login, navOptions)
                                showSnackbar(
                                    signupBinding.root,
                                    getString(R.string.account_created_successfully) ,
                                    Snackbar.LENGTH_LONG,
                                )
                            } else {
                                showSnackbar(
                                    signupBinding.root,
                                    task.exception?.message ?: getString(R.string.unable_to_create_account),
                                    Snackbar.LENGTH_LONG,
                                )
                            }
                        } else {
                            // If signing up fails, display a message to the user.
                            showSnackbar(
                                signupBinding.root,
                                task.exception?.message ?: getString(R.string.unable_to_create_account),
                                Snackbar.LENGTH_LONG,
                            )
                        }
                        signupBinding.signupProgress.visibility = View.GONE
                    }
            })
    }
}