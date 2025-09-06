package com.breastcancer.breastcancercare.states

sealed class BlogUIState<T>(val data: T? = null, val message: String? = null) {
    class Initial<T> : BlogUIState<T>()
    class Loading<T> : BlogUIState<T>()

    class Success<T>(data: T? = null) : BlogUIState<T>(data = data)

    class Error<T>(errorMessage: String? = null) : BlogUIState<T>(message = errorMessage)
}