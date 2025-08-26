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
import com.breastcancer.breastcancercare.screens.SubScreens
import com.breastcancer.breastcancercare.screens.main.AboutScreen
import com.breastcancer.breastcancercare.screens.main.ContactSupportScreen
import com.breastcancer.breastcancercare.screens.main.MainScreen
import com.breastcancer.breastcancercare.screens.onboarding.OnboardingScreen
import com.breastcancer.breastcancercare.utils.getNavigationRoute
import com.breastcancer.breastcancercare.viewmodel.PermissionViewModel
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import org.koin.compose.viewmodel.koinViewModel
import com.breastcancer.breastcancercare.screens.onboarding.RegisterScreen
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.PopUpTo




@Composable
fun App() {
    val loaderState = rememberLoaderState()
    val customSnackBarState = rememberSnackBarState()
    val permissionViewModel = koinViewModel<PermissionViewModel>()
    val permissionState by permissionViewModel.permissionState.collectAsStateWithLifecycle()
    val permissionImportantDialog by permissionViewModel.permissionImportantDialog.collectAsStateWithLifecycle()

    BindEffect(permissionViewModel.permissionsController)
    val coroutineScope = rememberCoroutineScope()
    val repo: com.breastcancer.breastcancercare.repo.OnboardingRepository =
        org.koin.compose.koinInject()
    val loggedInUser by repo.getLoggedInUser().collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(permissionState) {
        println("Permission State: ${permissionState.name}")
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
                        if (loggedInUser == null) {
                            val navigator = rememberNavigator()
                            NavHost(
                                navigator = navigator,
                                initialRoute = Screens.Onboarding.screen
                            ) {
                                scene(route = getNavigationRoute(mainScreen = Screens.Onboarding)) {
                                    OnboardingScreen(
                                        loaderState = loaderState,
                                        customSnackBarState = customSnackBarState,
                                        alreadyLoggedIn = { /* no-op */ },
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
                            }
                        } else {
                            val navigator = rememberNavigator()
                            NavHost(
                                navigator = navigator,
                                initialRoute = Screens.Main.screen
                            ) {
                                scene(route = getNavigationRoute(Screens.Main)) {
                                    MainScreen(
                                        permissionState = permissionState,
                                        loaderState = loaderState,
                                        customSnackBarState = customSnackBarState,
                                        onSubScreenChange = {
                                            navigator.navigate(
                                                getNavigationRoute(
                                                    mainScreen = Screens.Main,
                                                    subScreen = it
                                                )
                                            )
                                        },
                                        onLogOut = {
                                            coroutineScope.launch { repo.logOut() }
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
                                    val userDao: com.breastcancer.breastcancercare.database.local.dao.UserDao =
                                        org.koin.compose.koinInject()
                                    com.breastcancer.breastcancercare.screens.main.ProfileRoute(
                                        userDao = userDao,
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
                                    com.breastcancer.breastcancercare.screens.main.EditProfileRoute(
                                        onBack = { navigator.goBack() }
                                    )
                                }

                                scene(
                                    route = getNavigationRoute(
                                        mainScreen = Screens.Main,
                                        subScreen = SubScreens.About
                                    )
                                ) { AboutScreen { navigator.goBack() } }
                            }
                        }
                    }
                }
            }
        }
    }
}

