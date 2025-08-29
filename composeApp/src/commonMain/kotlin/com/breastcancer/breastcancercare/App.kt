package com.breastcancer.breastcancercare

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.components.BreastCancerAlertDialog
import com.breastcancer.breastcancercare.components.loader.CustomLoader
import com.breastcancer.breastcancercare.components.loader.rememberLoaderState
import com.breastcancer.breastcancercare.components.snackbar.CustomSnackBar
import com.breastcancer.breastcancercare.components.snackbar.rememberSnackBarState
import com.breastcancer.breastcancercare.screens.Screens
import com.breastcancer.breastcancercare.screens.SplashScreen
import com.breastcancer.breastcancercare.screens.SubScreens
import com.breastcancer.breastcancercare.screens.main.AboutScreen
import com.breastcancer.breastcancercare.screens.main.BlogDetailScreen
import com.breastcancer.breastcancercare.screens.main.ContactSupportScreen
import com.breastcancer.breastcancercare.screens.main.EditProfileRoute
import com.breastcancer.breastcancercare.screens.main.MainScreen
import com.breastcancer.breastcancercare.screens.main.ProfileRoute
import com.breastcancer.breastcancercare.screens.onboarding.OnboardingScreen
import com.breastcancer.breastcancercare.screens.onboarding.RegisterScreen
import com.breastcancer.breastcancercare.utils.getNavigationRoute
import com.breastcancer.breastcancercare.viewmodel.PermissionViewModel
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.rememberNavigator
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun App() {
    val loaderState = rememberLoaderState()
    val customSnackBarState = rememberSnackBarState()
    val permissionViewModel = koinViewModel<PermissionViewModel>()
    val permissionState by permissionViewModel.permissionState.collectAsStateWithLifecycle()
    val permissionImportantDialog by permissionViewModel.permissionImportantDialog.collectAsStateWithLifecycle()

    BindEffect(permissionViewModel.permissionsController)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(permissionState) {
        when (permissionState) {
            PermissionState.NotDetermined ->
                coroutineScope.launch {
                    permissionViewModel.onRequestPermissionButtonPressed()
                }

            PermissionState.Denied -> permissionViewModel.showDialog()
            else -> {

            }
        }
    }


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
                            initialRoute = Screens.Splash.screen
                        ) {
                            scene(route = getNavigationRoute(mainScreen = Screens.Splash)) {
                                SplashScreen(onAlreadyLoggedIn = {
                                    navigator.navigate(
                                        getNavigationRoute(mainScreen = Screens.Main),
                                        options = NavOptions(
                                            popUpTo = PopUpTo(
                                                route = getNavigationRoute(Screens.Splash),
                                                inclusive = true
                                            ),
                                            launchSingleTop = true
                                        )
                                    )
                                }, onNotLoggedIn = {
                                    navigator.navigate(
                                        getNavigationRoute(mainScreen = Screens.Onboarding),
                                        options = NavOptions(
                                            popUpTo = PopUpTo(
                                                route = getNavigationRoute(Screens.Splash),
                                                inclusive = true
                                            ),
                                            launchSingleTop = true
                                        )
                                    )
                                })
                            }
                            scene(route = getNavigationRoute(mainScreen = Screens.Onboarding)) {
                                OnboardingScreen(
                                    loaderState = loaderState,
                                    customSnackBarState = customSnackBarState,
                                    onLogin = {
                                        navigator.navigate(
                                            getNavigationRoute(mainScreen = Screens.Main),
                                            options = NavOptions(
                                                popUpTo = PopUpTo(
                                                    route = getNavigationRoute(Screens.Onboarding),
                                                    inclusive = true
                                                ),
                                                launchSingleTop = true
                                            )
                                        )
                                    },
                                    onRegister = {
                                        navigator.navigate(
                                            getNavigationRoute(
                                                mainScreen = Screens.Onboarding,
                                                subScreen = SubScreens.Register
                                            )
                                        )
                                    }
                                )
                            }
                            scene(
                                route = getNavigationRoute(
                                    mainScreen = Screens.Onboarding,
                                    subScreen = SubScreens.Register
                                )
                            ) {
                                RegisterScreen(
                                    customSnackBarState = customSnackBarState,
                                    loaderState = loaderState,
                                    onBack = { navigator.goBack() },
                                    registrationSuccessful = {
                                        navigator.navigate(
                                            getNavigationRoute(Screens.Onboarding),
                                            options = NavOptions(
                                                popUpTo = PopUpTo(
                                                    route = getNavigationRoute(Screens.Onboarding),
                                                    inclusive = true
                                                ),
                                                launchSingleTop = true
                                            )
                                        )
                                    }
                                )
                            }

                            scene(route = getNavigationRoute(Screens.Main)) {
                                MainScreen(
                                    permissionState = permissionState,
                                    loaderState = loaderState,
                                    customSnackBarState = customSnackBarState,
                                    onSubScreenChange = {
                                        navigator.navigate(
                                            getNavigationRoute(
                                                mainScreen = Screens.Main,
                                                subScreen = it.subScreen
                                            )
                                        )
                                    },
                                    onLogOut = {
                                        navigator.navigate(
                                            getNavigationRoute(mainScreen = Screens.Onboarding),
                                            options = NavOptions(
                                                popUpTo = PopUpTo(
                                                    route = getNavigationRoute(Screens.Main),
                                                    inclusive = true
                                                ),
                                                launchSingleTop = true
                                            )
                                        )
                                    }
                                )

                                if (permissionImportantDialog)
                                    BreastCancerAlertDialog(
                                        title = "Important!",
                                        text = {
                                            Text("It is very important that you grant the notifications permission to receive notifications regarding events and programs from us.\nPlease grant the permission to receive updates.")
                                        },
                                        confirmText = "Grant",
                                        dismissText = "Cancel",
                                        onDismissRequest = { permissionViewModel.dismissDialog() },
                                        onConfirm = {
                                            coroutineScope.launch {
                                                permissionViewModel.onRequestPermissionButtonPressed()
                                            }
                                            permissionViewModel.dismissDialog()
                                        }
                                    )
                            }

                            scene(
                                route = getNavigationRoute(
                                    mainScreen = Screens.Main,
                                    subScreen = SubScreens.Contact
                                )
                            ) { ContactSupportScreen { navigator.goBack() } }

                            scene(
                                route = getNavigationRoute(
                                    mainScreen = Screens.Main,
                                    subScreen = SubScreens.Profile
                                )
                            ) {
                                ProfileRoute(
                                    onBack = { navigator.goBack() },
                                    onEditProfile = {
                                        navigator.navigate(
                                            getNavigationRoute(
                                                mainScreen = Screens.Main,
                                                subScreen = SubScreens.EditProfile
                                            )
                                        )
                                    }
                                )
                            }

                            scene(
                                route = getNavigationRoute(
                                    mainScreen = Screens.Main,
                                    subScreen = SubScreens.EditProfile
                                )
                            ) {
                                EditProfileRoute(
                                    onBack = { navigator.goBack() }
                                )
                            }

                            scene(
                                route = getNavigationRoute(
                                    mainScreen = Screens.Main,
                                    subScreen = SubScreens.About
                                )
                            ) { AboutScreen { navigator.goBack() } }

                            scene(
                                route = getNavigationRoute(
                                    mainScreen = Screens.Main,
                                    subScreen = SubScreens.BlogDetail
                                )
                            ) {
                                BlogDetailScreen(onBack = {
                                    navigator.goBack()
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}

