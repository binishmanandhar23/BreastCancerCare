package com.breastcancer.breastcancercare

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.breastcancer.breastcancercare.screens.CalendarScreen
import com.breastcancer.breastcancercare.screens.HomeScreen
import com.breastcancer.breastcancercare.screens.Tabs
import com.breastcancer.breastcancercare.theme.DefaultElevation
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPadding
import com.breastcancer.breastcancercare.theme.DefaultVerticalPadding
import com.breastcancer.breastcancercare.theme.RoundedCornerSize
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun App() {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { Tabs.entries.size })
    val selectedTabIndex by remember(pagerState.currentPage) { derivedStateOf { pagerState.currentPage } }
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(modifier = Modifier.fillMaxSize(), state = pagerState) {
            when (Tabs.entries[selectedTabIndex].text) {
                Tabs.Home.text -> HomeScreen()
                Tabs.Calendar.text -> CalendarScreen()
            }
        }
        BottomBar(
            outerModifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(horizontal = 15.dp, vertical = 15.dp),
            innerModifier = Modifier.fillMaxWidth()
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
                scope.launch {
                    pagerState.animateScrollToPage(0)
                }
            },
            onCalendar = {
                scope.launch {
                    pagerState.animateScrollToPage(1)
                }
            }
        )
    }
}