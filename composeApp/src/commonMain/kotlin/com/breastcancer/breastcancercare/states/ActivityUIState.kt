package com.breastcancer.breastcancercare.states

sealed class ActivityUIState<T>(val data: T? = null, val message: String? = null) {
    class Initial<T> : ActivityUIState<T>()
    class Loading<T> : ActivityUIState<T>()

    class Success<T>(data: T? = null) : ActivityUIState<T>(data = data)

    class Error<T>(errorMessage: String? = null) : ActivityUIState<T>(message = errorMessage)
}