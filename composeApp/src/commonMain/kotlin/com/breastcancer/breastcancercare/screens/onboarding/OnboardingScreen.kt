package com.breastcancer.breastcancercare.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.breastcancer.breastcancercare.Res
import com.breastcancer.breastcancercare.app_name
import com.breastcancer.breastcancercare.breast_cancer_care_wa
import com.breastcancer.breastcancercare.components.loader.LoaderState
import com.breastcancer.breastcancercare.components.snackbar.SnackBarState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnboardingScreen(
    loaderState: LoaderState,
    customSnackBarState: SnackBarState,
    onLogin: () -> Unit,
    onRegister: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth().align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.breast_cancer_care_wa),
                contentDescription = stringResource(Res.string.app_name)
            )
            Text(modifier = Modifier.clickable(onClick = onLogin), text = stringResource(Res.string.app_name))
        }
    }
}