package com.breastcancer.breastcancercare.states

sealed class FAQUIState<T> (val data: T? = null, val message: String? = null){
    class Loading <T>: FAQUIState<T>()

    class Success <T>(data: T? = null): FAQUIState<T>(data = data)

    class Error<T>(errorMessage: String? = null): FAQUIState<T>(message = errorMessage)
}
