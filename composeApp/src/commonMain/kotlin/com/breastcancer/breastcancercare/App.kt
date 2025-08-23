package com.breastcancer.breastcancercare

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.breastcancer.breastcancercare.components.loader.CustomLoader
import com.breastcancer.breastcancercare.components.loader.rememberLoaderState
import com.breastcancer.breastcancercare.components.snackbar.CustomSnackBar
import com.breastcancer.breastcancercare.components.snackbar.rememberSnackBarState
import com.breastcancer.breastcancercare.screens.Screens
import com.breastcancer.breastcancercare.screens.SubScreens
import com.breastcancer.breastcancercare.screens.main.AboutScreen
import com.breastcancer.breastcancercare.screens.main.ContactSupportScreen
import com.breastcancer.breastcancercare.screens.main.MainScreen
import com.breastcancer.breastcancercare.screens.main.ProfileScreen
import com.breastcancer.breastcancercare.screens.onboarding.OnboardingScreen
import com.breastcancer.breastcancercare.utils.getNavigationRoute
import com.breastcancer.breastcancercare.viewmodel.PermissionViewModel
import dev.icerock.moko.permissions.compose.BindEffect
import kotlinx.coroutines.launch
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.logger.Logger

@Composable
fun App() {
    val loaderState = rememberLoaderState()
    val customSnackBarState = rememberSnackBarState()

    val permissionViewModel = koinViewModel<PermissionViewModel>()
    BindEffect(permissionViewModel.permissionsController)

    val coroutineScope = rememberCoroutineScope()

    Scaffold { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(top = innerPadding.calculateTopPadding())
        ) {
            MaterialTheme {
                CustomLoader(loaderState = loaderState) {
                    CustomSnackBar(
                        text = "",
                        snackBarState = customSnackBarState,
                        useBox = true
                    ) {
                        val navigator = rememberNavigator()
                        NavHost(
                            navigator = navigator,
                            initialRoute = Screens.Onboarding.screen
                        ) {
                            scene(route = getNavigationRoute(mainScreen = Screens.Onboarding)) {
                                val owner = LocalLifecycleOwner.current
                                println("Owner: $owner")
                                OnboardingScreen(
                                    loaderState = loaderState,
                                    customSnackBarState = customSnackBarState,
                                    onLogin = {
                                        navigator.navigate(Screens.Main.screen)
                                    },
                                    onRegister = {
                                        coroutineScope.launch {
                                            permissionViewModel.onRequestPermissionButtonPressed()
                                        }
                                    }
                                )
                            }
                            scene(route = getNavigationRoute(Screens.Main)) {
                                MainScreen(
                                    loaderState = loaderState,
                                    customSnackBarState = customSnackBarState,
                                    onSubScreenChange = {
                                        navigator.navigate(
                                            getNavigationRoute(
                                                mainScreen = Screens.Main,
                                                subScreen = it
                                            )
                                        )
                                    }
                                )
                            }
                            scene(
                                route = getNavigationRoute(
                                    mainScreen = Screens.Main,
                                    subScreen = SubScreens.Contact
                                )
                            ) {
                                ContactSupportScreen {
                                    navigator.goBack()
                                }
                            }
                            scene(
                                route = getNavigationRoute(
                                    mainScreen = Screens.Main,
                                    subScreen = SubScreens.Profile
                                )
                            ) {
                                ProfileScreen {
                                    navigator.goBack()
                                }
                            }
                            scene(
                                route = getNavigationRoute(
                                    mainScreen = Screens.Main,
                                    subScreen = SubScreens.About
                                )
                            ) {
                                AboutScreen {
                                    navigator.goBack()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

