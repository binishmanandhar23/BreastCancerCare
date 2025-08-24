package com.breastcancer.breastcancercare.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.Res
import com.breastcancer.breastcancercare.app_name
import com.breastcancer.breastcancercare.breast_cancer_care_wa
import com.breastcancer.breastcancercare.components.BreastCancerAlertDialog
import com.breastcancer.breastcancercare.components.BreastCancerButton
import com.breastcancer.breastcancercare.components.BreastCancerSingleLineTextField
import com.breastcancer.breastcancercare.components.loader.LoaderState
import com.breastcancer.breastcancercare.components.snackbar.SnackBarLengthMedium
import com.breastcancer.breastcancercare.components.snackbar.SnackBarState
import com.breastcancer.breastcancercare.states.LoginUIState
import com.breastcancer.breastcancercare.theme.DefaultVerticalPadding
import com.breastcancer.breastcancercare.utils.rememberWindowSizeDp
import com.breastcancer.breastcancercare.utils.text.ClickableText
import com.breastcancer.breastcancercare.utils.text.TextUtils
import com.breastcancer.breastcancercare.viewmodel.OnboardingViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingScreen(
    loaderState: LoaderState,
    customSnackBarState: SnackBarState,
    onboardingViewModel: OnboardingViewModel = koinViewModel(),
    alreadyLoggedIn: () -> Unit,
    onRegister: () -> Unit
) {
    val clickHereColor = MaterialTheme.colorScheme.secondary
    val userDTO by onboardingViewModel.userDTO.collectAsStateWithLifecycle()
    val password by onboardingViewModel.password.collectAsStateWithLifecycle()
    val emailValid by onboardingViewModel.emailValid.collectAsStateWithLifecycle()
    val windowSize = rememberWindowSizeDp()
    val loginUIState by onboardingViewModel.loginUIState.collectAsStateWithLifecycle()

    LaunchedEffect(loginUIState) {
        when (loginUIState) {
            is LoginUIState.Success -> {
                customSnackBarState.show(overridingText = loginUIState.message, overridingDelay = SnackBarLengthMedium)
                loaderState.hide()
                alreadyLoggedIn()
            }
            is LoginUIState.Error -> {
                customSnackBarState.show(overridingText = loginUIState.message, overridingDelay = SnackBarLengthMedium)
                loaderState.hide()
            }
            is LoginUIState.Loading -> loaderState.show()
            else -> loaderState.hide()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth().align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(DefaultVerticalPadding)
        ) {
            Image(
                modifier = Modifier.padding(vertical = DefaultVerticalPadding),
                painter = painterResource(Res.drawable.breast_cancer_care_wa),
                contentDescription = stringResource(Res.string.app_name)
            )
            BreastCancerSingleLineTextField(
                modifier = Modifier.width(windowSize.first / 1.3f),
                label = "Email",
                value = userDTO.email,
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Email, contentDescription = "Email")
                },
                onValueChange = { onboardingViewModel.updateUserDTO(userDTO = userDTO.copy(email = it)) },
                errorText = if (!emailValid) "Invalid email" else null,
                errorIcon = Icons.Default.Error,
            )
            BreastCancerSingleLineTextField(
                modifier = Modifier.width(windowSize.first / 1.3f),
                label = "Password",
                value = password,
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Password, contentDescription = "Password")
                },
                onValueChange = onboardingViewModel::updatePassword,
                visualTransformation = PasswordVisualTransformation()
            )
            BreastCancerButton(text = "Login", onClick = onboardingViewModel::onLogin)
            ClickableText(
                textStyle = TextStyle.Default.copy(fontSize = 12.sp),
                onClick = { tag ->
                    if (tag == "register") {
                        onRegister()
                    }
                }
            ) {
                append("New Member? ")
                withClickable("register") {
                    withStyle(
                        SpanStyle(
                            color = clickHereColor,
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(" Click here")
                    }
                }
                append(".")
            }
        }
    }
}