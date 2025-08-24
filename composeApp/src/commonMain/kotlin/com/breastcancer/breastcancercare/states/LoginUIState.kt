package com.breastcancer.breastcancercare.states

sealed class LoginUIState(val message: String? = null){

    object Initial: LoginUIState()
    object Loading: LoginUIState()
    class Success(successMessage: String? = null): LoginUIState(message = successMessage)
    class Error(errorMessage: String?): LoginUIState(message = errorMessage)

    object LoggedOut: LoginUIState()
}