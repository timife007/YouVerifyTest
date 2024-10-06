package com.timife.youverifytest.presentation.utils

//Validation logic for authentication screens
object Validation {

    private fun validateEmail(email: String): ValidateResponse {
        val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (!isEmailValid) {
            return ValidateResponse(false, "Enter a valid email")
        }
        return ValidateResponse(true, "Email validated")
    }

    private fun validatePassword(password: String, confirmPassword: String): ValidateResponse {
        // Check if the password length is less than 6 characters
        if (password.length < 6) {
            return ValidateResponse(
                isValid = false,
                message = "Password is too short, should be at least 6 characters",
                errorType = ErrorType.SHORT_PASSWORD
            )
        }

        // Check if the password and confirmPassword match
        if (password != confirmPassword) {
            return ValidateResponse(
                isValid = false,
                message = "Passwords don't match",
                errorType = ErrorType.PASSWORD_MISMATCH
            )
        }

        // If both validations pass, return a successful response
        return ValidateResponse(isValid = true, message = "Password Validated")
    }


    /**
     * @param isEmailValid: A callback that indicates if the email is valid and provides an optional error message.
     * @param isPasswordValid: A callback that indicates if the password is valid and provides an optional error message and a flag for the validity of the password.
     * @param allValidated: A callback that is invoked when both email and password are valid.
     */
    fun validateSignupCredentials(
        email: String,
        password: String,
        confirmPassword: String,
        isEmailValid: (Boolean, String?) -> Unit,
        isPasswordValid: (Boolean, String?, Boolean) -> Unit,
        allValidated: () -> Unit
    ) {
        // Validate email
        val emailValidation = validateEmail(email)
        isEmailValid(emailValidation.isValid, emailValidation.message)

        // Validate password and confirm password
        val passwordValidation = validatePassword(password, confirmPassword)
        if (passwordValidation.isValid) {
            isPasswordValid(true, null, true) // Valid password
        } else {
            // If password validation fails, check for specific error type
            isPasswordValid(
                false,
                passwordValidation.message,
                passwordValidation.errorType == ErrorType.SHORT_PASSWORD
            )
        }

        // Call allValidated if both email and password are valid
        if (emailValidation.isValid && passwordValidation.isValid) {
            allValidated()
        }
    }


    /**
     * @param isEmailValid: A callback that indicates if the email is valid and provides an optional error message.
     * @param isPasswordValid: A callback that indicates if the password is valid and provides an optional error message.
     * @param allValidated: A callback that is invoked when both email and password are valid.
     */
    fun validateLoginCredentials(
        email: String,
        password: String,
        isEmailValid: (Boolean, String?) -> Unit,
        isPasswordValid: (Boolean, String?) -> Unit,
        allValidated: () -> Unit
    ) {
        // Check for empty email
        if (email.isBlank()) {
            isEmailValid(false, "Email cannot be empty")
            return // Early return to prevent further validation
        }

        // Validate email format and return validation result
        val emailValidation = validateEmail(email)
        isEmailValid(emailValidation.isValid, emailValidation.message)

        // Check for empty password
        if (password.isBlank()) {
            isPasswordValid(false, "Password cannot be empty")
            return // Early return to prevent further validation
        }

        // Validate password length
        val isPasswordValidLength = password.length >= 6
        if (!isPasswordValidLength) {
            isPasswordValid(false, "Password should not be less than 6 characters")
        } else {
            isPasswordValid(true, null) // Password length is valid
        }

        // Call allValidated callback if both email and password are valid
        if (emailValidation.isValid && isPasswordValidLength) {
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