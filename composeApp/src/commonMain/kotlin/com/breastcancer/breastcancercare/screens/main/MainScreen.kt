package com.breastcancer.breastcancercare.screens.main

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.breastcancer.breastcancercare.components.BottomBar
import com.breastcancer.breastcancercare.components.loader.LoaderState
import com.breastcancer.breastcancercare.components.snackbar.SnackBarState
import com.breastcancer.breastcancercare.screens.SubScreens
import com.breastcancer.breastcancercare.screens.Tabs
import com.breastcancer.breastcancercare.states.LoginUIState
import com.breastcancer.breastcancercare.theme.DefaultElevation
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingSmall
import com.breastcancer.breastcancercare.theme.DefaultVerticalPadding
import com.breastcancer.breastcancercare.theme.RoundedCornerSize
import com.breastcancer.breastcancercare.viewmodel.OnboardingViewModel
import dev.icerock.moko.permissions.PermissionState
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    onboardingViewModel: OnboardingViewModel = koinViewModel(),
    permissionState: PermissionState,
    loaderState: LoaderState,
    customSnackBarState: SnackBarState,
    onSubScreenChange: (SubScreens) -> Unit,
    onLogOut: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { Tabs.entries.size })

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            userScrollEnabled = false,
            beyondViewportPageCount = 1
        ) { page ->
            when (Tabs.entries[page].text) {
                Tabs.Home.text -> HomeScreen()
                Tabs.Calendar.text -> CalendarScreen()
                Tabs.FAQ.text -> FAQScreen(
                    loaderState = loaderState,
                    snackBarState = customSnackBarState
                )

                Tabs.Settings.text -> SettingsScreen(
                    permissionState = permissionState,
                    customSnackBarState = customSnackBarState,
                    onOpenProfile = { onSubScreenChange(SubScreens.Profile) },
                    onOpenAbout = { onSubScreenChange(SubScreens.About) },
                    onContactSupport = { onSubScreenChange(SubScreens.Contact) },
                    onLogOut = {
                        onboardingViewModel.onLogOut()
                        onLogOut()
                    }
                )
            }
        }

        BottomBar(
            outerModifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 15.dp, vertical = 15.dp),
            innerModifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = DefaultElevation,
                    shape = RoundedCornerShape(RoundedCornerSize)
                )
                .background(color = MaterialTheme.colorScheme.background)
                .padding(
                    PaddingValues(
                        horizontal = DefaultHorizontalPaddingSmall,
                        vertical = DefaultVerticalPadding
                    )
                ),
            page = pagerState.currentPage,
            onHome = {
                scope.launch { pagerState.animateScrollToPage(Tabs.Home.ordinal) }
            },
            onCalendar = {
                scope.launch { pagerState.animateScrollToPage(Tabs.Calendar.ordinal) }
            },
            onFAQ = {
                scope.launch { pagerState.animateScrollToPage(Tabs.FAQ.ordinal) }
            },
            onSettings = {
                scope.launch { pagerState.animateScrollToPage(Tabs.Settings.ordinal) }
            }
        )
    }
}