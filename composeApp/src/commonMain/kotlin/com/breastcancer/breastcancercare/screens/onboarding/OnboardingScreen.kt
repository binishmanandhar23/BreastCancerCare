package com.breastcancer.breastcancercare.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.Res
import com.breastcancer.breastcancercare.app_name
import com.breastcancer.breastcancercare.breast_cancer_care_wa
import com.breastcancer.breastcancercare.components.BreastCancerButton
import com.breastcancer.breastcancercare.components.BreastCancerSingleLineTextField
import com.breastcancer.breastcancercare.components.loader.LoaderState
import com.breastcancer.breastcancercare.components.snackbar.SnackBarState
import com.breastcancer.breastcancercare.theme.DefaultVerticalPadding
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
    onLogin: () -> Unit,
    onRegister: () -> Unit
) {
    val clickHereColor = MaterialTheme.colorScheme.secondary
    val email by onboardingViewModel.email.collectAsStateWithLifecycle()
    val password by onboardingViewModel.password.collectAsStateWithLifecycle()

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
            BreastCancerSingleLineTextField(label = "Email", value = email, onValueChange = onboardingViewModel::setEmail)
            BreastCancerSingleLineTextField(
                label = "Password",
                value = password,
                onValueChange = onboardingViewModel::setPassword,
                visualTransformation = PasswordVisualTransformation()
            )
            BreastCancerButton(text = "Login", onClick = onLogin)
            ClickableText(textStyle = TextStyle.Default.copy(fontSize = 12.sp), onClick = {
                onRegister()
            }) {
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