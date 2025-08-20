package com.breastcancer.breastcancercare

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.breastcancer.breastcancercare.components.BottomBar
import com.breastcancer.breastcancercare.components.loader.CustomLoader
import com.breastcancer.breastcancercare.components.loader.rememberLoaderState
import com.breastcancer.breastcancercare.components.snackbar.CustomSnackBar
import com.breastcancer.breastcancercare.components.snackbar.rememberSnackBarState
import com.breastcancer.breastcancercare.screens.main.CalendarScreen
import com.breastcancer.breastcancercare.screens.main.FAQScreen
import com.breastcancer.breastcancercare.screens.main.HomeScreen
import com.breastcancer.breastcancercare.screens.Screens
import com.breastcancer.breastcancercare.screens.main.SettingsScreen
import com.breastcancer.breastcancercare.screens.Tabs
import com.breastcancer.breastcancercare.screens.main.MainScreen
import com.breastcancer.breastcancercare.screens.onboarding.OnboardingScreen
import com.breastcancer.breastcancercare.theme.DefaultElevation
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPadding
import com.breastcancer.breastcancercare.theme.DefaultVerticalPadding
import com.breastcancer.breastcancercare.theme.RoundedCornerSize
import kotlinx.coroutines.launch
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator

@Composable
fun App() {

    val loaderState = rememberLoaderState()
    val customSnackBarState = rememberSnackBarState()

    Scaffold { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(top = innerPadding.calculateTopPadding())
        ) {
            PreComposeApp {
                val navigator = rememberNavigator()
                MaterialTheme {
                    CustomLoader(loaderState = loaderState) {
                        CustomSnackBar(
                            text = "",
                            snackBarState = customSnackBarState,
                            useBox = true
                        ) {
                            NavHost(
                                navigator = navigator,
                                initialRoute = Screens.Onboarding.screen
                            ) {
                                scene(route = Screens.Onboarding.screen) {
                                    OnboardingScreen(
                                        loaderState = loaderState,
                                        customSnackBarState = customSnackBarState,
                                        onLogin = {
                                            navigator.navigate(Screens.Main.screen)
                                        },
                                        onRegister = {
                                        }
                                    )
                                }
                                scene(route = Screens.Main.screen) {
                                    MainScreen(
                                        loaderState = loaderState,
                                        customSnackBarState = customSnackBarState
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

