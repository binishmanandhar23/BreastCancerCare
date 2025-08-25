package com.breastcancer.breastcancercare.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.breastcancer.breastcancercare.Greeting
import com.breastcancer.breastcancercare.Res
import com.breastcancer.breastcancercare.components.DefaultSpacerSize
import com.breastcancer.breastcancercare.compose_multiplatform
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreen(){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding()
            .animateContentSize()
            ,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        item {
            DefaultSpacerSize()
        }
    }
}