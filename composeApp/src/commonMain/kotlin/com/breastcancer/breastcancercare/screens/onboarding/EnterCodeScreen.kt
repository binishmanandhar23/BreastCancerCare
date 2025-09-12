package com.breastcancer.breastcancercare.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.components.BreastCancerBackButton
import com.breastcancer.breastcancercare.components.BreastCancerButton
import com.breastcancer.breastcancercare.components.BreastCancerSingleLineTextField
import com.breastcancer.breastcancercare.components.loader.LoaderState
import com.breastcancer.breastcancercare.components.snackbar.SnackBarLengthMedium
import com.breastcancer.breastcancercare.components.snackbar.SnackBarState
import com.breastcancer.breastcancercare.states.LoginUIState
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.viewmodel.OnboardingViewModel

@Composable
fun EnterCodeScreen(
    onboardingViewModel: OnboardingViewModel,
    customSnackBarState: SnackBarState,
    loaderState: LoaderState,
    registrationSuccessful: () -> Unit,
    onBack: () -> Unit
) {
    val code by onboardingViewModel.code.collectAsStateWithLifecycle()
    val loginUIState by onboardingViewModel.loginUIState.collectAsStateWithLifecycle()
    LaunchedEffect(loginUIState) {
        when (loginUIState) {
            is LoginUIState.RegistrationSuccessful -> {
                customSnackBarState.show(
                    overridingText = loginUIState.message,
                    overridingDelay = SnackBarLengthMedium
                )
                loaderState.hide()
                onboardingViewModel.clearTransientLoginState()
                registrationSuccessful()
            }

            is LoginUIState.Error -> {
                customSnackBarState.show(
                    overridingText = loginUIState.message,
                    overridingDelay = SnackBarLengthMedium
                )
                loaderState.hide()
            }

            is LoginUIState.Loading -> loaderState.show()
            else -> loaderState.hide()
        }
    }
    Box(modifier = Modifier.fillMaxSize().padding(
        horizontal = DefaultHorizontalPaddingMedium,
        vertical = DefaultVerticalPaddingMedium
    )) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = DefaultHorizontalPaddingMedium),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(bottom = DefaultHorizontalPaddingMedium),
                text = "Please enter the code provided to you via email/sms.",
                style = MaterialTheme.typography.headlineLarge.copy(textAlign = TextAlign.Center)
            )
            BreastCancerSingleLineTextField(
                modifier = Modifier.padding(bottom = DefaultHorizontalPaddingMedium),
                value = code,
                onValueChange = onboardingViewModel::updateCode,
                label = "Enter your six-digit code."
            )
            BreastCancerButton(
                text = "Submit",
                onClick = onboardingViewModel::onCodeSubmitted,
                enabled = code.length == 6, onDisabledClick = {
                    customSnackBarState.show(
                        overridingText = "Code needs to be of 6 characters.",
                        overridingDelay = SnackBarLengthMedium
                    )
                }
            )
        }
        BreastCancerBackButton(onBackClick = onBack)
    }
}