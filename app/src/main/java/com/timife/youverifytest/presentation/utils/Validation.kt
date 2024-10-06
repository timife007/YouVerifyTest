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

        if (password.length < 6) {
            return ValidateResponse(
                isValid = false,
                message = "Password is too short, should be at least 6 characters",
                errorType = ErrorType.SHORT_PASSWORD
            )
        } else if (password != confirmPassword) {
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
        val emailValidation = validateEmail(email)
        val passwordValidation =
            validatePassword(password, confirmPassword)
        // Check if the email is valid
        if (!emailValidation.isValid) {
            // If invalid, invoke callback with false and the error message
            isEmailValid(false, emailValidation.message)
            return
        }
        // If valid, invoke callback indicating success
        isEmailValid(true, null)

        // Check if the password is valid
        if (!passwordValidation.isValid) {
            /**
             *If invalid, invoke callback with error message and
             * whether it's a short password password error.
             */
            if (passwordValidation.errorType == ErrorType.SHORT_PASSWORD) {
                isPasswordValid(false, passwordValidation.message, true)
            } else {
                isPasswordValid(true, passwordValidation.message, false)
            }
            return
        }
        // If valid, invoke callback indicating success
        isPasswordValid(true, null, true)

        // Call the allValidated callback since all validations passed
        allValidated()
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