package com.breastcancer.breastcancercare.states

sealed class ActivityUIState<T>(val data: T? = null, val message: String? = null, val registrationUIState: Success.RegistrationUIState<T>? = null) {
    class Initial<T> : ActivityUIState<T>()
    class Loading<T> : ActivityUIState<T>()

    class Success<T>(
        data: T? = null,
        registrationUIState: RegistrationUIState<T> = RegistrationUIState.Initial<T>()
    ) : ActivityUIState<T>(data = data, registrationUIState = registrationUIState) {
        sealed class RegistrationUIState<T> {
            class Initial<T> : RegistrationUIState<T>()
            class Registering<T> : RegistrationUIState<T>()
            class Registered<T>(data: T? = null) : RegistrationUIState<T>()
        }
    }

    class Error<T>(errorMessage: String? = null) : ActivityUIState<T>(message = errorMessage)
}