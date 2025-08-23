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
import com.breastcancer.breastcancercare.theme.DefaultElevation
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPadding
import com.breastcancer.breastcancercare.theme.DefaultVerticalPadding
import com.breastcancer.breastcancercare.theme.RoundedCornerSize
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    loaderState: LoaderState,
    customSnackBarState: SnackBarState,
    onSubScreenChange: (SubScreens) -> Unit
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { Tabs.entries.size })

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
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
                    onOpenProfile = { onSubScreenChange(SubScreens.Profile) },
                    onOpenAbout = { onSubScreenChange(SubScreens.About) },
                    onContactSupport = { onSubScreenChange(SubScreens.Contact) }
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
                        horizontal = DefaultHorizontalPadding,
                        vertical = DefaultVerticalPadding
                    )
                ),
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