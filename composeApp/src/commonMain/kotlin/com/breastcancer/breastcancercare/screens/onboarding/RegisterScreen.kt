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
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.utils.ensureHttpScheme
import com.breastcancer.breastcancercare.utils.text.ClickableText
import com.breastcancer.breastcancercare.utils.text.removeSpaces

@Composable
fun RegisterScreen(
    onboardingViewModel: OnboardingViewModel = koinViewModel(),
    customSnackBarState: SnackBarState,
    loaderState: LoaderState,
    onBack: () -> Unit,
    onRegister: () -> Unit
) {
    val (screenW, _) = rememberWindowSizeDp()

    val uriHandler = LocalUriHandler.current
    val termsOfUseURL = remember { "https://www.breastcancer.org.au/terms-and-conditions/" }
    val privacyPolicyURL = remember { "https://www.breastcancer.org.au/wp-content/uploads/2025/09/BCCWA-Privacy-Policy-020424.-Sept-2025-FINAL.pdf" }

    val pw by onboardingViewModel.password.collectAsStateWithLifecycle()
    val confirm by onboardingViewModel.confirmPassword.collectAsStateWithLifecycle()
    val agree by onboardingViewModel.agree.collectAsStateWithLifecycle()
    val userDTO by onboardingViewModel.userDTO.collectAsStateWithLifecycle()
    val loginUIState by onboardingViewModel.loginUIState.collectAsStateWithLifecycle()
    val phoneValid by onboardingViewModel.phoneValid.collectAsStateWithLifecycle()
    val emailValidInstant by onboardingViewModel.emailValid.collectAsStateWithLifecycle()
    val passwordValidInstant by onboardingViewModel.passwordValid.collectAsStateWithLifecycle()
    val canRegister by onboardingViewModel.canRegister.collectAsStateWithLifecycle()

    val tfColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        cursorColor = MaterialTheme.colorScheme.primary
    )
    val borderNormal = MaterialTheme.colorScheme.outline
    val borderFocused = MaterialTheme.colorScheme.primary
    val termsOfUseColor = MaterialTheme.colorScheme.secondary

    val formWidth = (screenW * 0.88f).coerceAtMost(520.dp)

    LaunchedEffect(loginUIState) {
        when (loginUIState) {
            is LoginUIState.RegistrationSuccessful -> {
                customSnackBarState.show(overridingText = loginUIState.message, overridingDelay = SnackBarLengthMedium)
                loaderState.hide()
                onboardingViewModel.clearTransientLoginState()
                onRegister()
            }
            is LoginUIState.Error -> {
                customSnackBarState.show(overridingText = loginUIState.message, overridingDelay = SnackBarLengthMedium)
                loaderState.hide()
            }
            is LoginUIState.Loading -> loaderState.show()
            else -> loaderState.hide()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 28.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        BreastCancerToolbar(title = "Create your account", onBack = onBack)
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
                    onValueChange = { onboardingViewModel.updateUserDTO(userDTO = userDTO.copy(email = it.removeSpaces())) },
                    colors = tfColors,
                    borderColor = borderNormal,
                    focusedBorderColor = borderFocused,
                    errorText = if (userDTO.email.isNotBlank() && !emailValidInstant) "Invalid email" else null,
                    errorIcon = Icons.Default.Error,
                )

                BreastCancerSingleLineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Phone number",
                    value = userDTO.phoneNumber,
                    leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = "Phone") },
                    onValueChange = { onboardingViewModel.updateUserDTO(userDTO.copy(phoneNumber = it.removeSpaces())) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                    colors = tfColors,
                    borderColor = borderNormal,
                    focusedBorderColor = borderFocused,
                    errorText = if (!phoneValid && userDTO.phoneNumber.isNotBlank()) "Invalid phone number" else null,
                    errorIcon = Icons.Default.Error
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
                    errorText = if ((pw.isNotBlank() || confirm.isNotBlank()) && !passwordValidInstant)
                        "Passwords must match and be at least 6 characters long."
                    else null,
                    errorIcon = Icons.Default.Error,
                )

                BreastCancerSingleLineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Confirm password",
                    value = confirm,
                    leadingIcon = { Icon(Icons.Outlined.Password, contentDescription = "Confirm") },
                    onValueChange = onboardingViewModel::updateConfirmPassword,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    colors = tfColors,
                    borderColor = borderNormal,
                    focusedBorderColor = borderFocused,
                    errorText = if ((pw.isNotBlank() || confirm.isNotBlank()) && !passwordValidInstant)
                        "Passwords must match and be at least 6 characters long."
                    else null,
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
                    ClickableText(
                        textStyle = TextStyle.Default.copy(fontSize = 12.sp),
                        onClick = { tag ->
                            when(tag) {
                                "terms_and_conditions" -> {
                                    val url = termsOfUseURL.ensureHttpScheme()
                                    runCatching { uriHandler.openUri(url) }
                                }
                                "privacy_policy" -> {
                                    val url = privacyPolicyURL.ensureHttpScheme()
                                    runCatching { uriHandler.openUri(url) }
                                }
                            }
                        }
                    ) {
                        append("I agree to the ")
                        withClickable("privacy_policy") {
                            withStyle(
                                SpanStyle(
                                    color = termsOfUseColor,
                                    textDecoration = TextDecoration.Underline,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Privacy Policy")
                            }
                        }
                        append(" and ")
                        withClickable("terms_and_conditions") {
                            withStyle(
                                SpanStyle(
                                    color = termsOfUseColor,
                                    textDecoration = TextDecoration.Underline,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Terms of Use")
                            }
                        }
                        append(".")
                    }
                }
            }
        }

        Box(
            modifier = Modifier.width(formWidth),
            contentAlignment = Alignment.Center
        ) {
            BreastCancerButton(
                text = "Create account",
                enabled = canRegister && loginUIState !is LoginUIState.Loading,
                onClick = { onboardingViewModel.onRegister() },
                onDisabledClick = {
                    customSnackBarState.show(
                        overridingText = "Please check email, phone, password and agree to the Terms.",
                        overridingDelay = SnackBarLengthMedium
                    )
                }
            )
        }
    }
}
