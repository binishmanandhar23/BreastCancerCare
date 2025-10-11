package com.breastcancer.breastcancercare.states

sealed class TutorialUIState<T>(val data: T? = null, val message: String? = null) {
    class Initial<T> : TutorialUIState<T>()

    class Loading<T> : TutorialUIState<T>()

    class Success<T>(data: T? = null) : TutorialUIState<T>(data = data)

    class Error<T>(errorMessage: String? = null) : TutorialUIState<T>(message = errorMessage)
}