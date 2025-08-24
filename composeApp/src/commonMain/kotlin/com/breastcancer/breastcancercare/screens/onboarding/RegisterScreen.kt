package com.breastcancer.breastcancercare.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.breastcancer.breastcancercare.components.BreastCancerButton
import com.breastcancer.breastcancercare.components.BreastCancerSingleLineTextField
import com.breastcancer.breastcancercare.utils.rememberWindowSizeDp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import com.breastcancer.breastcancercare.components.BreastCancerToolbar
import com.breastcancer.breastcancercare.components.loader.LoaderState
import com.breastcancer.breastcancercare.components.snackbar.SnackBarLengthMedium
import com.breastcancer.breastcancercare.components.snackbar.SnackBarState
import com.breastcancer.breastcancercare.states.LoginUIState
import com.breastcancer.breastcancercare.viewmodel.OnboardingViewModel
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    onboardingViewModel: OnboardingViewModel = koinViewModel(),
    customSnackBarState: SnackBarState,
    loaderState: LoaderState,
    onBack: () -> Unit,
    registrationSuccessful: () -> Unit
) {
    val (screenW, _) = rememberWindowSizeDp()

    val pw by onboardingViewModel.password.collectAsStateWithLifecycle()
    val confirm by onboardingViewModel.confirmPassword.collectAsStateWithLifecycle()
    val agree by onboardingViewModel.agree.collectAsStateWithLifecycle()
    val passwordValid by onboardingViewModel.passwordValid.collectAsStateWithLifecycle()
    val emailValid by onboardingViewModel.emailValid.collectAsStateWithLifecycle()
    val userDTO by onboardingViewModel.userDTO.collectAsStateWithLifecycle()
    val loginUIState by onboardingViewModel.loginUIState.collectAsStateWithLifecycle()

    LaunchedEffect(loginUIState) {
        when (loginUIState) {
            is LoginUIState.Success -> {
                customSnackBarState.show(overridingText = loginUIState.message, overridingDelay = SnackBarLengthMedium)
                loaderState.hide()
                registrationSuccessful()
            }
            is LoginUIState.Error -> {
                customSnackBarState.show(overridingText = loginUIState.message, overridingDelay = SnackBarLengthMedium)
                loaderState.hide()
            }
            is LoginUIState.Loading -> loaderState.show()
            else -> loaderState.hide()
        }
    }

    val tfColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        cursorColor = MaterialTheme.colorScheme.primary
    )
    val borderNormal = MaterialTheme.colorScheme.outline
    val borderFocused = MaterialTheme.colorScheme.primary

    val formWidth = (screenW * 0.88f).coerceAtMost(520.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 28.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        BreastCancerToolbar(onBack = onBack)
        Card(
            modifier = Modifier.width(formWidth),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BreastCancerSingleLineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "First name",
                    value = userDTO.firstName,
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = "First name"
                        )
                    },
                    onValueChange = { onboardingViewModel.updateUserDTO(userDTO.copy(firstName = it)) },
                    colors = tfColors,
                    borderColor = borderNormal,
                    focusedBorderColor = borderFocused
                )

                BreastCancerSingleLineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Last name",
                    value = userDTO.lastName,
                    leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = "Last name") },
                    onValueChange = { onboardingViewModel.updateUserDTO(userDTO.copy(lastName = it)) },
                    colors = tfColors,
                    borderColor = borderNormal,
                    focusedBorderColor = borderFocused
                )

                BreastCancerSingleLineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Email",
                    value = userDTO.email,
                    leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = "Email") },
                    onValueChange = { onboardingViewModel.updateUserDTO(userDTO = userDTO.copy(email = it)) },
                    colors = tfColors,
                    borderColor = borderNormal,
                    focusedBorderColor = borderFocused,
                    errorText = if (!emailValid) "Invalid email" else null,
                    errorIcon = Icons.Default.Error,
                )

                BreastCancerSingleLineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Password",
                    value = pw,
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Password,
                            contentDescription = "Password"
                        )
                    },
                    onValueChange = onboardingViewModel::updatePassword,
                    visualTransformation = PasswordVisualTransformation(),
                    colors = tfColors,
                    borderColor = borderNormal,
                    focusedBorderColor = borderFocused,
                    errorText = if (!passwordValid) "Passwords must match and be at least 6 characters long." else null,
                    errorIcon = Icons.Default.Error,
                )

                BreastCancerSingleLineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Confirm password",
                    value = confirm,
                    leadingIcon = { Icon(Icons.Outlined.Password, contentDescription = "Confirm") },
                    onValueChange = onboardingViewModel::updateConfirmPassword,
                    visualTransformation = PasswordVisualTransformation(),
                    colors = tfColors,
                    borderColor = borderNormal,
                    focusedBorderColor = borderFocused,
                    errorText = if (!passwordValid) "Passwords must match and be at least 6 characters long." else null,
                    errorIcon = Icons.Default.Error,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Checkbox(checked = agree, onCheckedChange = onboardingViewModel::toggleAgree)
                    Text("I agree to the Terms & Privacy")
                }
            }
        }

        Box(
            modifier = Modifier.width(formWidth),
            contentAlignment = Alignment.Center
        ) {
            BreastCancerButton(
                text = "Create account",
                enabled = agree,
                onClick = onboardingViewModel::onSubmit,
                onDisabledClick = {
                    customSnackBarState.show(
                        overridingText = "Please agree to the Terms & Conditions before proceeding.",
                        overridingDelay = SnackBarLengthMedium
                    )
                }
            )
        }
    }
}
