package com.breastcancer.breastcancercare.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.components.BottomBar
import com.breastcancer.breastcancercare.components.loader.LoaderState
import com.breastcancer.breastcancercare.components.snackbar.SnackBarState
import com.breastcancer.breastcancercare.database.local.types.GeneralActivityType
import com.breastcancer.breastcancercare.database.local.types.UserCategory
import com.breastcancer.breastcancercare.models.FontSizeEnum
import com.breastcancer.breastcancercare.screens.Route
import com.breastcancer.breastcancercare.screens.Tabs
import com.breastcancer.breastcancercare.theme.DefaultElevation
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingSmall
import com.breastcancer.breastcancercare.theme.DefaultSpacerSize
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.theme.RoundedCornerSize
import com.breastcancer.breastcancercare.utils.rememberIsLandscape
import com.breastcancer.breastcancercare.viewmodel.CalendarViewModel
import com.breastcancer.breastcancercare.viewmodel.HomeViewModel
import com.breastcancer.breastcancercare.viewmodel.OnboardingViewModel
import com.breastcancer.breastcancercare.viewmodel.SettingsViewModel
import dev.icerock.moko.permissions.PermissionState
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import com.breastcancer.breastcancercare.database.local.types.GeneralActivityType.Companion.GeneralActivityTypeEnum
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium

@Composable
fun MainScreen(
    onboardingViewModel: OnboardingViewModel = koinViewModel(),
    homeViewModel: HomeViewModel = koinViewModel(),
    calendarViewModel: CalendarViewModel = koinViewModel(),
    settingsViewModel: SettingsViewModel = koinViewModel(),
    permissionState: PermissionState,
    loaderState: LoaderState,
    customSnackBarState: SnackBarState,
    onEnableNotifications:() -> Unit,
    onSubScreenChange: (route: Route, clearStack: Boolean) -> Unit,
    onLogOut: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { Tabs.entries.size })

    val loggedInUser by homeViewModel.loggedInUser.collectAsStateWithLifecycle()
    val isLandscape = rememberIsLandscape()

    val fontSizeEnum by settingsViewModel.fontSize.collectAsStateWithLifecycle()
    val bottomSpacer by remember(isLandscape) {
        derivedStateOf {
            if (isLandscape) 0.dp else DefaultSpacerSize
        }
    }
    val startSpacer by remember(isLandscape, fontSizeEnum) {
        derivedStateOf { if (isLandscape)
            when(fontSizeEnum.second){
                FontSizeEnum.Small -> DefaultSpacerSize * 2
                FontSizeEnum.Medium -> (DefaultSpacerSize.value * 2.3).dp
                FontSizeEnum.Large -> (DefaultSpacerSize.value * 2.7).dp
            }
        else
            0.dp
        }
    }


    LaunchedEffect(loggedInUser) {
        loggedInUser?.let { user ->
            if(!user.tutorialViewed)
                onSubScreenChange(Route.TutorialScreen(userId = user.id), true)
            else if (user.userCategory == UserCategory.Undefined)
                onSubScreenChange(Route.Journey(userId = user.id, hideBackButton = true), true)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            modifier = Modifier.fillMaxSize().padding(start = startSpacer),
            state = pagerState,
            userScrollEnabled = false,
            beyondViewportPageCount = 1
        ) { page ->
            when (Tabs.entries[page].text) {
                Tabs.Home.text -> HomeScreen(
                    homeViewModel = homeViewModel,
                    bottomSpacer = bottomSpacer,
                    onBlogClick = {
                        onSubScreenChange(Route.Main.BlogDetail(slug = it.slug), false)
                    },
                    onAllBlogs = {
                        onSubScreenChange(Route.Main.AllBlogs, false)
                    },
                    onActivityClick = {
                        onSubScreenChange(Route.Main.ActivityDetail(id = it.id), false)
                    },
                    onAllSchedules = {
                        onSubScreenChange(Route.Main.AllActivityHistory, false)
                    },
                    onAllActivities = {
                        onSubScreenChange(Route.Main.AllActivities, false)
                    })

                Tabs.Calendar.text -> CalendarScreen(
                    bottomSpacer = bottomSpacer,
                    calendarViewModel = calendarViewModel,
                    onAddNursing = {
                        onSubScreenChange(Route.Main.GeneralActivityDetail(type = GeneralActivityTypeEnum.Nursing.type), false)
                    },
                    onAddCounselling = {
                        onSubScreenChange(Route.Main.GeneralActivityDetail(type = GeneralActivityTypeEnum.Counselling.type), false)
                    },
                    onSubScreenChange = onSubScreenChange
                )

                Tabs.FAQ.text -> FAQScreen(
                    loaderState = loaderState,
                    bottomSpacer = bottomSpacer,
                    snackBarState = customSnackBarState
                )

                Tabs.Settings.text -> SettingsScreen(
                    bottomSpacer = bottomSpacer,
                    permissionState = permissionState,
                    settingsViewModel = settingsViewModel,
                    customSnackBarState = customSnackBarState,
                    onOpenProfile = { onSubScreenChange(Route.Main.Profile, false) },
                    onOpenAbout = { onSubScreenChange(Route.Main.About, false) },
                    onContactSupport = { onSubScreenChange(Route.Main.Contact, false) },
                    onEnableNotifications = onEnableNotifications,
                    onSwitchJourney = {
                        loggedInUser?.let { user ->
                            onSubScreenChange(
                                Route.Journey(
                                    userId = user.id,
                                    userCategory = user.userCategory.category,
                                    hideBackButton = false
                                ), false
                            )
                        }
                    },
                    onTutorial = {
                        loggedInUser?.let { user ->
                            onSubScreenChange(Route.TutorialScreen(userId = user.id, hideSkipButton = true), false)
                        }
                    },
                    onLogOut = {
                        onboardingViewModel.onLogOut()
                        onLogOut()
                    }
                )
            }
        }

        BottomBar(
            outerModifier = Modifier.let {
                if (isLandscape)
                    it.fillMaxHeight()
                else
                    it.fillMaxWidth()
            }.align(if (isLandscape) Alignment.CenterStart else Alignment.BottomCenter)
                .padding(horizontal = DefaultHorizontalPaddingMedium, vertical = 5.dp),
            innerModifier = Modifier
                .let {
                    if (isLandscape)
                        it.fillMaxHeight()
                    else
                        it.fillMaxWidth()
                }
                .shadow(
                    elevation = DefaultElevation,
                    shape = RoundedCornerShape(RoundedCornerSize)
                )
                .background(color = MaterialTheme.colorScheme.background)
                .padding(
                    PaddingValues(
                        horizontal = DefaultHorizontalPaddingSmall,
                        vertical = DefaultVerticalPaddingMedium
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
            },
            onAddNursing = {
                onSubScreenChange(Route.Main.GeneralActivityDetail(type = GeneralActivityTypeEnum.Nursing.type), false)
            },
            onAddCounselling = {
                onSubScreenChange(Route.Main.GeneralActivityDetail(type = GeneralActivityTypeEnum.Counselling.type), false)
            }
        )
    }
}