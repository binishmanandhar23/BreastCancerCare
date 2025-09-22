package com.breastcancer.breastcancercare.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.Res
import com.breastcancer.breastcancercare.app_name
import com.breastcancer.breastcancercare.breast_cancer_care_wa
import com.breastcancer.breastcancercare.states.SplashUIState
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.viewmodel.SplashViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel,
    onAlreadyLoggedIn: () -> Unit,
    onNotLoggedIn: () -> Unit
) {
    val splashUIState by splashViewModel.splashUIState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit){
        splashViewModel.checkLoginStatus()
    }

    LaunchedEffect(splashUIState) {
        when (splashUIState) {
            is SplashUIState.Initial, is SplashUIState.Finish -> Unit /*Wait for the result*/
            is SplashUIState.LoggedIn -> onAlreadyLoggedIn()
            else -> onNotLoggedIn().also { splashViewModel.finish() }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth().align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                DefaultVerticalPaddingMedium
            )
        ) {
            Image(
                modifier = Modifier.scale(0.5f),
                painter = painterResource(Res.drawable.breast_cancer_care_wa),
                contentDescription = stringResource(Res.string.app_name)
            )
            /*Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center)
            )*/
        }
    }
}