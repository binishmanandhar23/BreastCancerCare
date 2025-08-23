package com.breastcancer.breastcancercare

import androidx.compose.ui.window.ComposeUIViewController
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.rememberNavigator

fun MainViewController() = ComposeUIViewController {
    PreComposeApp {
        App()
    }
}