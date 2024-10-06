package com.timife.youverifytest.presentation.utils

//Validation logic for authentication screens
object Validation {

    private fun validateEmail(email: String): ValidateResponse {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        if (!email.matches(emailRegex.toRegex())) {
            return ValidateResponse(false, "Enter a valid email")
        }
        return ValidateResponse(true, "Email validated")
    }

    private fun validatePassword(password: String, confirmPassword: String): ValidateResponse {
        if (password.length < 6) {
            return ValidateResponse(
                false,
                "Password is too short, should be at least 6 characters",
                ErrorType.SHORT_PASSWORD
            )
        }else if (password != confirmPassword) {
            return ValidateResponse(false, "Passwords don't match", ErrorType.PASSWORD_MISMATCH)
        }
        return ValidateResponse(true, "Password Validated")
    }

    fun areSignupCredentialsValid(
        email: String,
        password: String,
        confirmPassword: String,
        isEmailValid: (Boolean, String?) -> Unit,
        isPasswordValid: (Boolean, String?, Boolean) -> Unit,
        allValidated: () -> Unit
    ) {

        val emailValidation = validateEmail(email)
        val passwordValidation =
            validatePassword(password, confirmPassword)
        if (!emailValidation.isValid) {
            isEmailValid(false,emailValidation.message)
        }else{
            isEmailValid(true, null)
        }

        if (!passwordValidation.isValid) {
            if (passwordValidation.errorType == ErrorType.SHORT_PASSWORD){
                isPasswordValid(false,passwordValidation.message, true)
            }else{
                isPasswordValid(true, passwordValidation.message, false)
            }
        }else{
            isPasswordValid(true, null, true)
        }
        if(emailValidation.isValid && passwordValidation.isValid){
            allValidated()
        }
    }

    fun areLoginCredentialsValid(
        email: String,
        password: String,
        isEmailValid: (Boolean, String?) -> Unit,
        isPasswordValid: (Boolean, String?) -> Unit,
        allValidated: () -> Unit
    ) {
        val emailValidation = validateEmail(email)
        val passwordValidation = password.length >= 6
        if (!emailValidation.isValid) {
            isEmailValid(false, emailValidation.message)
        } else {
            isEmailValid(true, null)
        }

        if (!passwordValidation) {
            isPasswordValid(false, "Password should not be less than 6 characters")
        } else {
            isPasswordValid(true, null)
        }

        if(emailValidation.isValid && passwordValidation){
            allValidated()
        }
    }
}


data class ValidateResponse(
    val isValid: Boolean,
    val message: String,
    val errorType: ErrorType? = null
)

enum class ErrorType {
    SHORT_PASSWORD,
    PASSWORD_MISMATCH
}