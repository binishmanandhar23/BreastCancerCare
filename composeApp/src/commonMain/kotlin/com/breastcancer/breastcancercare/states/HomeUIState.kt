package com.breastcancer.breastcancercare.states

sealed class HomeUIState<T> (val data: T? = null, val message: String? = null){
    class Initial<T> : HomeUIState<T>()

    class Loading<T> : HomeUIState<T>()

    class Empty<T> : HomeUIState<T>()

    class Success <T>(data: T? = null): HomeUIState<T>(data = data)

    class Error<T>(errorMessage: String? = null): HomeUIState<T>(message = errorMessage)
}