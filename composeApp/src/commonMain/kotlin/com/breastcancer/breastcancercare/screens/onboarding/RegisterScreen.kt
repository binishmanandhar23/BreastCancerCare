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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.selection.toggleable

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
    val userDTO by onboardingViewModel.userDTO.collectAsStateWithLifecycle()
    val loginUIState by onboardingViewModel.loginUIState.collectAsStateWithLifecycle()
    val phoneValid by onboardingViewModel.phoneValid.collectAsStateWithLifecycle()
    val emailValidInstant by onboardingViewModel.emailValidInstant.collectAsStateWithLifecycle()
    val passwordLengthValid by onboardingViewModel.passwordLengthValid.collectAsStateWithLifecycle()
    val passwordsMatch by onboardingViewModel.passwordsMatch.collectAsStateWithLifecycle()
    val canRegister by onboardingViewModel.canRegister.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    LaunchedEffect(loginUIState) {
        when (loginUIState) {
            is LoginUIState.RegistrationSuccessful -> {
                customSnackBarState.show(overridingText = loginUIState.message, overridingDelay = SnackBarLengthMedium)
                loaderState.hide()
                onboardingViewModel.clearTransientLoginState()
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

    val colors = MaterialTheme.colorScheme
    val tfColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        cursorColor = colors.primary
    )
    val borderNormal = colors.outline
    val borderFocused = colors.primary
    val formWidth = remember(screenW) { (screenW * 0.88f).coerceAtMost(520.dp) }
    val emailError by remember(emailValidInstant, userDTO.email) { derivedStateOf { userDTO.email.isNotBlank() && !emailValidInstant } }
    val phoneError by remember(phoneValid, userDTO.phoneNumber) { derivedStateOf { userDTO.phoneNumber.isNotBlank() && !phoneValid } }
    val pwError by remember(passwordLengthValid, pw) { derivedStateOf { pw.isNotBlank() && !passwordLengthValid } }
    val confirmError by remember(passwordsMatch, confirm) { derivedStateOf { confirm.isNotBlank() && !passwordsMatch } }
    val canSubmit by remember(canRegister, loginUIState) { derivedStateOf { canRegister && loginUIState !is LoginUIState.Loading } }
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 12.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        BreastCancerToolbar(onBack = onBack)
        Spacer(Modifier.height(12.dp))
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
                    onValueChange = onboardingViewModel::updateEmail,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    colors = tfColors,
                    borderColor = borderNormal,
                    focusedBorderColor = borderFocused,
                    errorText = if (emailError) "Invalid email" else null,
                    errorIcon = Icons.Default.Error,
                )

                BreastCancerSingleLineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Phone number",
                    value = userDTO.phoneNumber,
                    leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = "Phone") },
                    onValueChange = onboardingViewModel::updatePhone,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                    colors = tfColors,
                    borderColor = borderNormal,
                    focusedBorderColor = borderFocused,
                    errorText = if (phoneError) "Invalid phone number" else null,
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                        onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
                    ),
                    colors = tfColors,
                    borderColor = borderNormal,
                    focusedBorderColor = borderFocused,
                    errorText = if (pwError) "At least 6 characters." else null,
                    errorIcon = Icons.Default.Error,
                )

                BreastCancerSingleLineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Confirm password",
                    value = confirm,
                    leadingIcon = { Icon(Icons.Outlined.Password, contentDescription = "Confirm") },
                    onValueChange = onboardingViewModel::updateConfirmPassword,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                        onDone = {
                            if (canSubmit) {
                                onboardingViewModel.onRegister()
                            }
                        }
                    ),
                    colors = tfColors,
                    borderColor = borderNormal,
                    focusedBorderColor = borderFocused,
                    errorText = if (confirmError) "Passwords must match." else null,
                    errorIcon = Icons.Default.Error,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .toggleable(
                            value = agree,
                            onValueChange = onboardingViewModel::toggleAgree,
                            role = androidx.compose.ui.semantics.Role.Checkbox
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Checkbox(checked = agree, onCheckedChange = null)
                    Text("I agree to the Terms & Privacy")
                }
            }
        }

        Box(
            modifier = Modifier
                .width(formWidth)
                .navigationBarsPadding()
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {
            BreastCancerButton(
                text = "Create account",
                enabled = canSubmit,
                onClick = onboardingViewModel::onRegister,
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
