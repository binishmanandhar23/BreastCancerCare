package com.breastcancer.breastcancercare.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.breastcancer.breastcancercare.components.icons.Counselling
import com.breastcancer.breastcancercare.components.icons.Nurse
import com.breastcancer.breastcancercare.screens.Tabs
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultSpacerSize
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.utils.rememberIsLandscape

@Composable
fun BottomBar(
    outerModifier: Modifier = Modifier.fillMaxWidth(),
    innerModifier: Modifier = Modifier.fillMaxWidth(),
    page: Int,
    onHome: () -> Unit,
    onCalendar: () -> Unit,
    onFAQ: () -> Unit,
    onSettings: () -> Unit,
    onAddNursing: () -> Unit,
    onAddCounselling: () -> Unit
) {
    var buttonSize by remember { mutableStateOf(DpSize(0.dp, 0.dp)) }
    val selectedPage by remember(page) { mutableStateOf(Tabs.entries[page].text) }

    var expanded by remember { mutableStateOf(false) }
    val fabAngle by animateFloatAsState(if (expanded) -45f else 0f, animationSpec = spring())
    val isLandscape = rememberIsLandscape()

    val homeColor by animateColorAsState(targetValue = getColor(selectedPage == Tabs.Home.text))
    val activitiesColor by animateColorAsState(targetValue = getColor(selectedPage == Tabs.Calendar.text))
    val infoColor by animateColorAsState(targetValue = getColor(selectedPage == Tabs.FAQ.text))
    val settingsColor by animateColorAsState(targetValue = getColor(selectedPage == Tabs.Settings.text))
    val content: @Composable () -> Unit = {
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
            text = "Activities",
            tint = activitiesColor,
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
    /*val fabButton: @Composable () -> Unit = {
        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            onClick = { expanded = !expanded },
            shape = CircleShape
        ) {
            Icon(
                modifier = Modifier.rotate(fabAngle),
                imageVector = Icons.Default.Add,
                contentDescription = "Add an Activity"
            )
        }
    }
    val fabContent: @Composable () -> Unit = {
        Button(
            onClick = onAddNursing,
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.onTertiary,
                containerColor = MaterialTheme.colorScheme.tertiary
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Nurse,
                    contentDescription = "Book a Nursing session"
                )
                Text(text = "Nursing")
            }
        }
        Button(
            onClick = onAddCounselling,
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.onTertiary,
                containerColor = MaterialTheme.colorScheme.tertiary
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Counselling,
                    contentDescription = "Book a Counselling Session"
                )
                Text(text = "Counselling")
            }
        }
    }
    val fabVisibilityContainer: @Composable () -> Unit = {
        AnimatedVisibility(
            visible = expanded,
            enter = (
                    if (isLandscape) slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = spring()
                    ) else slideInHorizontally(
                        initialOffsetX = { it / 2 },
                        animationSpec = spring()
                    )) + scaleIn(
                transformOrigin = TransformOrigin(
                    if (isLandscape) 0f else 0.5f,
                    if (isLandscape) 0f else 1f
                )
            ) + fadeIn(),
            exit = (if (isLandscape) slideOutVertically(
                targetOffsetY = { it / 2 },
                animationSpec = spring()
            ) else
                slideOutHorizontally(
                    targetOffsetX = { it / 2 },
                    animationSpec = spring()
                )) + scaleOut(
                transformOrigin = TransformOrigin(
                    if (isLandscape) 0f else 0f,
                    if (isLandscape) 0f else 1f
                )
            ) + fadeOut()
        ) {
            if (isLandscape)
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(DefaultHorizontalPaddingMedium)
                ) {
                    fabContent()
                }
            else
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingMedium)
                ) {
                    fabContent()
                }
        }
    }*/
    Box(modifier = outerModifier) {
        if (isLandscape)
            Column(
                modifier = innerModifier.align(Alignment.CenterStart),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        else
            Row(
                modifier = innerModifier.align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                content()
            }
    }

    //Old Fab Design
    /*Box(modifier = outerModifier) {
        if (isLandscape)
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = innerModifier.weight(0.8f),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        content()
                    }
                    Row(
                        modifier = Modifier.weight(0.2f),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        fabButton()
                    }
                }
                fabVisibilityContainer()
            }
        else
            Column(
                modifier = Modifier.align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.End
            ) {
                fabVisibilityContainer()
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = innerModifier.weight(0.82f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        content()
                    }
                    Column(
                        modifier = Modifier.weight(0.18f),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        fabButton()
                    }
                }
            }
        *//*CenterButton(modifier = Modifier.padding(bottom = 50.dp).align(Alignment.TopCenter), onSizeChange = { size ->
            density.convertIntSizeToDpSize(size){
                buttonSize = it
            }
        }, onClick = {

        })*//*
    }*/
}

@Composable
fun getColor(selected: Boolean) =
    if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground

@Composable
fun DefaultSpacerSize() = Spacer(modifier = Modifier.padding(DefaultSpacerSize))