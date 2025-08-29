package com.breastcancer.breastcancercare.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.breastcancer.breastcancercare.screens.Tabs
import com.breastcancer.breastcancercare.theme.DefaultSpacerSize

@Composable
fun BottomBar(
    outerModifier: Modifier = Modifier.fillMaxWidth(),
    innerModifier: Modifier = Modifier.fillMaxWidth(),
    page: Int,
    onHome: () -> Unit,
    onCalendar: () -> Unit,
    onFAQ: () -> Unit,
    onSettings: () -> Unit = {}
) {
    var buttonSize by remember { mutableStateOf(DpSize(0.dp, 0.dp)) }
    val selectedPage by remember(page) { mutableStateOf(Tabs.entries[page].text) }

    val homeColor by animateColorAsState(targetValue = getColor(selectedPage == Tabs.Home.text))
    val eventsColor by animateColorAsState(targetValue = getColor(selectedPage == Tabs.Calendar.text))
    val infoColor by animateColorAsState(targetValue = getColor(selectedPage == Tabs.FAQ.text))
    val settingsColor by animateColorAsState(targetValue = getColor(selectedPage == Tabs.Settings.text))
    Box(modifier = outerModifier) {
        Row(
            modifier = innerModifier.align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomBarIcon(
                modifier = Modifier,
                imageVector = Icons.Default.Home,
                text = "Home",
                tint = homeColor,
                onClick = onHome
            )
            BottomBarIcon(
                modifier = Modifier,
                imageVector = Icons.Default.Event,
                text = "Events",
                tint = eventsColor,
                onClick = onCalendar
            )
            Spacer(Modifier.size(buttonSize))
            BottomBarIcon(
                modifier = Modifier,
                imageVector = Icons.Default.Info,
                text = "Info",
                tint = infoColor,
                onClick = onFAQ
            )
            BottomBarIcon(
                modifier = Modifier,
                imageVector = Icons.Default.Settings,
                text = "Settings",
                tint = settingsColor,
                onClick = onSettings
            )
        }

        /*CenterButton(modifier = Modifier.padding(bottom = 50.dp).align(Alignment.TopCenter), onSizeChange = { size ->
            density.convertIntSizeToDpSize(size){
                buttonSize = it
            }
        }, onClick = {

        })*/
    }
}

@Composable
fun getColor(selected: Boolean) = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground

@Composable
fun DefaultSpacerSize() = Spacer(modifier = Modifier.padding(DefaultSpacerSize))