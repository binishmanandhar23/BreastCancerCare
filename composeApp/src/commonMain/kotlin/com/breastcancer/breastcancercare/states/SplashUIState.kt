package com.breastcancer.breastcancercare.states

sealed class SplashUIState {
    object Initial: SplashUIState()
    object LoggedIn: SplashUIState()
    object NotLoggedIn: SplashUIState()
}