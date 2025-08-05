package com.breastcancer.breastcancercare.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.breastcancer.breastcancercare.theme.DefaultSpacerSize
import com.breastcancer.breastcancercare.theme.DefaultVerticalPadding

@Composable
fun BottomBar(outerModifier: Modifier = Modifier.fillMaxWidth(), innerModifier: Modifier = Modifier.fillMaxWidth(),  onHome: () -> Unit, onCalendar: () -> Unit) {
    val density = LocalDensity.current
    var buttonSize by remember { mutableStateOf(DpSize(0.dp, 0.dp)) }
    Box(modifier = outerModifier) {
        Row(
            modifier = innerModifier.align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomBarIcon(imageVector = Icons.Default.Home, text = "Home", onClick = onHome)
            BottomBarIcon(imageVector = Icons.Default.CalendarMonth, text = "Calendar", onClick = onCalendar)
            Spacer(Modifier.size(buttonSize))
            BottomBarIcon(imageVector = Icons.Default.QuestionAnswer, text = "FAQ")
            BottomBarIcon(imageVector = Icons.Default.Settings, text = "Settings")
        }
        CenterButton(modifier = Modifier.padding(bottom = 50.dp).align(Alignment.TopCenter), onSizeChange = { size ->
            density.convertIntSizeToDpSize(size){
                buttonSize = it
            }
        }, onClick = {

        })
    }
}

@Composable
fun DefaultSpacerSize() = Spacer(modifier = Modifier.padding(DefaultSpacerSize))