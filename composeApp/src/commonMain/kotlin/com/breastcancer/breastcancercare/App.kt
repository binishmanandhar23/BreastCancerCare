package com.breastcancer.breastcancercare

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.breastcancer.breastcancercare.components.BreastCancerAlertDialog
import com.breastcancer.breastcancercare.components.loader.CustomLoader
import com.breastcancer.breastcancercare.components.loader.rememberLoaderState
import com.breastcancer.breastcancercare.components.snackbar.CustomSnackBar
import com.breastcancer.breastcancercare.components.snackbar.rememberSnackBarState
import com.breastcancer.breastcancercare.screens.Route
import com.breastcancer.breastcancercare.screens.SplashScreen
import com.breastcancer.breastcancercare.screens.main.AboutScreen
import com.breastcancer.breastcancercare.screens.main.BlogDetailScreen
import com.breastcancer.breastcancercare.screens.main.ContactSupportScreen
import com.breastcancer.breastcancercare.screens.main.EditProfileRoute
import com.breastcancer.breastcancercare.screens.main.MainScreen
import com.breastcancer.breastcancercare.screens.main.ProfileScreen
import com.breastcancer.breastcancercare.screens.onboarding.OnboardingScreen
import com.breastcancer.breastcancercare.screens.onboarding.RegisterScreen
import com.breastcancer.breastcancercare.viewmodel.PermissionViewModel
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import kotlinx.coroutines.launch
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
            CustomLoader(loaderState = loaderState) {
                CustomSnackBar(
                    text = "",
                    snackBarState = customSnackBarState,
                    useBox = true
                ) {
                    val navigator = rememberNavController()
                    NavHost(
                        navController = navigator,
                        startDestination = Route.Splash,
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(300)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(300)
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(300)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(300)
                            )
                        }) {
                        composable<Route.Splash> {
                            SplashScreen(onAlreadyLoggedIn = {
                                navigator.navigate(route = Route.Main) {
                                    popUpTo(route = Route.Splash) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }, onNotLoggedIn = {
                                navigator.navigate(route = Route.Onboarding) {
                                    popUpTo(route = Route.Splash) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            })
                        }
                        composable<Route.Onboarding> {
                            OnboardingScreen(
                                loaderState = loaderState,
                                customSnackBarState = customSnackBarState,
                                onLogin = {
                                    navigator.navigate(Route.Main) {
                                        popUpTo(route = Route.Onboarding) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                },
                                onRegister = {
                                    navigator.navigate(
                                        route = Route.Onboarding.Register
                                    )
                                }
                            )
                        }
                        composable<Route.Onboarding.Register> {
                            RegisterScreen(
                                customSnackBarState = customSnackBarState,
                                loaderState = loaderState,
                                onBack = { navigator.popBackStack() },
                                registrationSuccessful = {
                                    navigator.navigate(Route.Onboarding) {
                                        popUpTo(route = Route.Onboarding) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }

                        composable<Route.Main> {
                            MainScreen(
                                permissionState = permissionState,
                                loaderState = loaderState,
                                customSnackBarState = customSnackBarState,
                                onSubScreenChange = {
                                    navigator.navigate(
                                        route = it
                                    )
                                },
                                onLogOut = {
                                    navigator.navigate(Route.Onboarding) {
                                        popUpTo(route = Route.Main) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                },
                                onOpenGuideDetail = {
                                    navigator.navigate(Route.Main.GuideDetail)
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


                        composable<Route.Main.Contact> { ContactSupportScreen { navigator.popBackStack() } }

                        composable<Route.Main.Profile> {
                            ProfileScreen(
                                onBack = { navigator.popBackStack() },
                                onEditProfile = {
                                    navigator.navigate(
                                        route = Route.Main.EditProfile
                                    )
                                }
                            )
                        }

                        composable<Route.Main.EditProfile> {
                            EditProfileRoute(
                                onBack = { navigator.popBackStack() }
                            )
                        }
                        composable<Route.Main.BlogDetail> {
                            BlogDetailScreen(onBack = {
                                navigator.popBackStack()
                            })
                        }

                        composable<Route.Main.About> { AboutScreen { navigator.popBackStack() } }

                        composable<Route.Main.GuideDetail> {
                            val vm =
                                koinViewModel<com.breastcancer.breastcancercare.viewmodel.FAQViewModel>()
                            val guide = vm.selectedGuide.collectAsStateWithLifecycle().value
                            if (guide != null) {
                                com.breastcancer.breastcancercare.screens.main.GuideDetailScreen(
                                    guide = guide,
                                    onBack = { navigator.popBackStack() }
                                )
                            } else {
                                LaunchedEffect(Unit) { navigator.popBackStack() }
                            }
                        }

                    }
                }
            }
        }
    }
}

