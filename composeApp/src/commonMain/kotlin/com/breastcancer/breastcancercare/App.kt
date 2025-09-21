package com.breastcancer.breastcancercare

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.breastcancer.breastcancercare.components.BreastCancerAlertDialog
import com.breastcancer.breastcancercare.components.loader.CustomLoader
import com.breastcancer.breastcancercare.components.loader.rememberLoaderState
import com.breastcancer.breastcancercare.components.snackbar.CustomSnackBar
import com.breastcancer.breastcancercare.components.snackbar.rememberSnackBarState
import com.breastcancer.breastcancercare.database.local.types.UserCategory
import com.breastcancer.breastcancercare.screens.Route
import com.breastcancer.breastcancercare.screens.SplashScreen
import com.breastcancer.breastcancercare.screens.journey.JourneyDetailScreen
import com.breastcancer.breastcancercare.screens.main.AboutScreen
import com.breastcancer.breastcancercare.screens.main.activity.ActivityDetailScreen
import com.breastcancer.breastcancercare.screens.main.activity.AllActivitiesScreen
import com.breastcancer.breastcancercare.screens.main.AllBlogsScreen
import com.breastcancer.breastcancercare.screens.main.BlogDetailScreen
import com.breastcancer.breastcancercare.screens.main.ContactSupportScreen
import com.breastcancer.breastcancercare.screens.main.EditProfileRoute
import com.breastcancer.breastcancercare.screens.main.MainScreen
import com.breastcancer.breastcancercare.screens.main.ProfileRoute
import com.breastcancer.breastcancercare.screens.main.survey.SurveyScreen
import com.breastcancer.breastcancercare.screens.journey.JourneyScreen
import com.breastcancer.breastcancercare.screens.main.activity.ActivityHistoryScreen
import com.breastcancer.breastcancercare.screens.main.survey.SurveyMandatoryDialogScreen
import com.breastcancer.breastcancercare.screens.onboarding.OnboardingScreen
import com.breastcancer.breastcancercare.screens.onboarding.RegisterScreen
import com.breastcancer.breastcancercare.theme.BreastCareTypography
import com.breastcancer.breastcancercare.theme.LightAppColorScheme
import com.breastcancer.breastcancercare.viewmodel.ActivityViewModel
import com.breastcancer.breastcancercare.viewmodel.BlogViewModel
import com.breastcancer.breastcancercare.viewmodel.HomeViewModel
import com.breastcancer.breastcancercare.viewmodel.OnboardingViewModel
import com.breastcancer.breastcancercare.viewmodel.PermissionViewModel
import com.breastcancer.breastcancercare.viewmodel.SplashViewModel
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun App() {
    val darkTheme = isSystemInDarkTheme()
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

    MaterialTheme(
        colorScheme = if (darkTheme) LightAppColorScheme else LightAppColorScheme,
        typography = BreastCareTypography()
    ) {
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
                            startDestination = Route.BaseGraph,
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

                            navigation<Route.BaseGraph>(startDestination = Route.Splash) {
                                composable<Route.Splash> { backStackEntry ->
                                    val vm = koinViewModel<SplashViewModel>()
                                    SplashScreen(
                                        splashViewModel = vm,
                                        onAlreadyLoggedIn = {
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
                                composable<Route.Onboarding> { backStackEntry ->
                                    OnboardingScreen(
                                        onboardingViewModel = koinViewModel<OnboardingViewModel>(
                                            viewModelStoreOwner = navigator.getBackStackEntry(Route.BaseGraph)
                                        ),
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
                                        onboardingViewModel = koinViewModel<OnboardingViewModel>(
                                            viewModelStoreOwner = navigator.getBackStackEntry(Route.BaseGraph)
                                        ),
                                        customSnackBarState = customSnackBarState,
                                        loaderState = loaderState,
                                        onBack = { navigator.popBackStack() }, onRegister = {
                                            navigator.navigate(Route.Onboarding) {
                                                popUpTo(route = Route.Onboarding) {
                                                    inclusive = true
                                                }
                                                launchSingleTop = true
                                            }
                                        }
                                    )
                                }

                                composable<Route.Journey> { backStackEntry ->
                                    val userId = backStackEntry.toRoute<Route.Journey>().userId
                                    val hideBackButton =
                                        backStackEntry.toRoute<Route.Journey>().hideBackButton
                                    JourneyScreen(
                                        userId = userId,
                                        customSnackBarState = customSnackBarState,
                                        hideBackButton = hideBackButton,
                                        onNext = { userId, userCategory ->
                                            navigator.navigate(
                                                Route.Journey.JourneyDetail(
                                                    userId = userId,
                                                    userCategory = userCategory.category
                                                )
                                            )
                                        }, onBack = {
                                            navigator.popBackStack()
                                        }
                                    )
                                }

                                composable<Route.Journey.JourneyDetail> { backStackEntry ->
                                    val userId =
                                        backStackEntry.toRoute<Route.Journey.JourneyDetail>().userId
                                    val userCategory =
                                        backStackEntry.toRoute<Route.Journey.JourneyDetail>().userCategory
                                    JourneyDetailScreen(
                                        homeViewModel = koinViewModel<HomeViewModel>(
                                            viewModelStoreOwner = navigator.getBackStackEntry(Route.BaseGraph)
                                        ),
                                        userId = userId,
                                        userCategory = UserCategory.fromCategory(
                                            category = userCategory
                                        ),
                                        customSnackBarState = customSnackBarState,
                                        onBack = {
                                            navigator.popBackStack()
                                        },
                                        onJourneyComplete = {
                                            navigator.navigate(Route.Main) {
                                                popUpTo(route = Route.Journey(userId = userId)) {
                                                    inclusive = true
                                                }
                                                launchSingleTop = true
                                            }
                                        }
                                    )
                                }

                                composable<Route.Main> {
                                    MainScreen(
                                        homeViewModel = koinViewModel<HomeViewModel>(
                                            viewModelStoreOwner = navigator.getBackStackEntry(Route.BaseGraph)
                                        ),
                                        onboardingViewModel = koinViewModel<OnboardingViewModel>(
                                            viewModelStoreOwner = navigator.getBackStackEntry(Route.BaseGraph)
                                        ),
                                        permissionState = permissionState,
                                        loaderState = loaderState,
                                        customSnackBarState = customSnackBarState,
                                        onSubScreenChange = { route, clearStack ->
                                            navigator.navigate(route = route) {
                                                if (clearStack)
                                                    popUpTo(route = Route.Main) {
                                                        inclusive = true
                                                    }.also {
                                                        launchSingleTop = true
                                                    }
                                            }
                                        },
                                        onLogOut = {
                                            navigator.navigate(Route.Onboarding) {
                                                popUpTo(route = Route.Main) {
                                                    inclusive = true
                                                }
                                                launchSingleTop = true
                                            }
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
                                    ProfileRoute(
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

                                composable<Route.Main.About> { AboutScreen { navigator.popBackStack() } }

                                composable<Route.Main.BlogDetail> { backStackEntry ->
                                    val parentEntry =
                                        remember(backStackEntry) { navigator.getBackStackEntry(Route.Main) }
                                    val blogViewModel = koinViewModel<BlogViewModel>(
                                        viewModelStoreOwner = parentEntry
                                    )
                                    val slug = backStackEntry.toRoute<Route.Main.BlogDetail>().slug
                                    BlogDetailScreen(
                                        loaderState = loaderState,
                                        slug = slug,
                                        blogViewModel = blogViewModel,
                                        onBack = {
                                            navigator.popBackStack()
                                        })
                                }

                                composable<Route.Main.AllBlogs> { backStackEntry ->
                                    val parentEntry =
                                        remember(backStackEntry) { navigator.getBackStackEntry(Route.Main) }
                                    val blogViewModel = koinViewModel<BlogViewModel>(
                                        viewModelStoreOwner = parentEntry
                                    )
                                    AllBlogsScreen(
                                        blogViewModel = blogViewModel,
                                        loaderState = loaderState,
                                        onBackPress = { navigator.popBackStack() },
                                        onSubScreenChange = {
                                            navigator.navigate(it)
                                        })
                                }

                                composable<Route.Main.AllActivities> { backStackEntry ->
                                    val parentEntry =
                                        remember(backStackEntry) { navigator.getBackStackEntry(Route.Main) }
                                    val activityViewModel = koinViewModel<ActivityViewModel>(
                                        viewModelStoreOwner = parentEntry
                                    )
                                    AllActivitiesScreen(
                                        activityViewModel = activityViewModel,
                                        onBackPress = { navigator.popBackStack() },
                                        onSubScreenChange = {
                                            navigator.navigate(it)
                                        })
                                }

                                composable<Route.Main.ActivityDetail> { backStackEntry ->
                                    val id = backStackEntry.toRoute<Route.Main.ActivityDetail>().id
                                    val activityViewModel = koinViewModel<ActivityViewModel>(
                                        viewModelStoreOwner = navigator.getBackStackEntry<Route.Main>()
                                    )
                                    ActivityDetailScreen(
                                        id = id,
                                        activityViewModel = activityViewModel, onBack = {
                                            navigator.popBackStack()
                                        }, onRegister = { activity ->
                                            activity.surveys?.preSurvey?.let { preSurvey ->
                                                navigator.navigate(
                                                    route = if (preSurvey.mandatory) Route.Main.SurveyMandatoryDialog(
                                                        id = activity.id
                                                    ) else Route.Main.SurveyRoute(
                                                        id = activity.id
                                                    )
                                                )
                                                return@ActivityDetailScreen
                                            }
                                            activityViewModel.insertActivityHistory(activity = activity)
                                        }
                                    )
                                }

                                composable<Route.Main.SurveyRoute> { backStackEntry ->
                                    val parentEntry =
                                        remember(backStackEntry) { navigator.getBackStackEntry(Route.Main) }
                                    val activityViewModel = koinViewModel<ActivityViewModel>(
                                        viewModelStoreOwner = parentEntry
                                    )
                                    val activityId =
                                        backStackEntry.toRoute<Route.Main.SurveyRoute>().id
                                    SurveyScreen(
                                        activityViewModel = activityViewModel,
                                        id = activityId,
                                        loaderState = loaderState,
                                        onBack = {
                                            navigator.popBackStack()
                                        }, onSurveySubmit = { activity, answers ->
                                            activityViewModel.insertActivityHistory(
                                                activity = activity,
                                                preSurveyAnswer = answers
                                            )
                                        }, onSkipped = { activity ->
                                            activityViewModel.insertActivityHistory(
                                                activity = activity
                                            )
                                        })
                                }

                                composable<Route.Main.AllActivityHistory> { backStackEntry ->
                                    val parentEntry =
                                        remember(backStackEntry) { navigator.getBackStackEntry(Route.Main) }
                                    val activityViewModel = koinViewModel<ActivityViewModel>(
                                        viewModelStoreOwner = parentEntry
                                    )
                                    ActivityHistoryScreen(
                                        activityViewModel = activityViewModel,
                                        onBackPress = { navigator.popBackStack() },
                                        onSubScreenChange = {
                                            navigator.navigate(it)
                                        }
                                    )
                                }

                                dialog<Route.Main.SurveyMandatoryDialog> { backStackEntry ->
                                    val id = backStackEntry.toRoute<Route.Main.SurveyRoute>().id
                                    SurveyMandatoryDialogScreen {
                                        navigator.navigate(Route.Main.SurveyRoute(id = id))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

