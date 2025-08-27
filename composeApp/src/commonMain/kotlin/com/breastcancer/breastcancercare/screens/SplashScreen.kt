package com.breastcancer.breastcancercare.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.breastcancer.breastcancercare.Res
import com.breastcancer.breastcancercare.app_name
import com.breastcancer.breastcancercare.breast_cancer_care_wa
import com.breastcancer.breastcancercare.states.SplashUIState
import com.breastcancer.breastcancercare.theme.DefaultVerticalPadding
import com.breastcancer.breastcancercare.viewmodel.SplashViewModel
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = koinInject(),
    onAlreadyLoggedIn: () -> Unit,
    onNotLoggedIn: () -> Unit
) {
    val splashUIState by splashViewModel.splashUIState.collectAsStateWithLifecycle()

    LaunchedEffect(splashUIState) {
        when (splashUIState) {
            is SplashUIState.Initial -> Unit /*Wait for the next state
            */
            is SplashUIState.LoggedIn -> onAlreadyLoggedIn()
            else -> onNotLoggedIn()
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth().align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                DefaultVerticalPadding
            )
        ) {
            Image(
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