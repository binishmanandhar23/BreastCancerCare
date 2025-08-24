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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color

@Composable
fun RegisterScreen(
    onBack: () -> Unit = {}
) {
    val (screenW, _) = rememberWindowSizeDp()

    var first by rememberSaveable { mutableStateOf("") }
    var last by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var pw by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }
    var agree by rememberSaveable { mutableStateOf(false) }

    val emailValid = remember(email) { email.contains("@") && email.contains(".") }
    val pwMatch = remember(pw, confirm) { pw == confirm }
    val pwTooShort = remember(pw) { pw.isNotEmpty() && pw.length < 6 }
    val canSubmit = first.isNotBlank() && last.isNotBlank() &&
            emailValid && pw.length >= 6 && pwMatch && agree
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
        Text("Create your account", style = MaterialTheme.typography.headlineLarge)

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
                    value = first,
                    leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = "First name") },
                    onValueChange = { first = it },
                    colors = tfColors,
                    borderColor = borderNormal,
                    focusedBorderColor = borderFocused
                )

                BreastCancerSingleLineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Last name",
                    value = last,
                    leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = "Last name") },
                    onValueChange = { last = it },
                    colors = tfColors,
                    borderColor = borderNormal,
                    focusedBorderColor = borderFocused
                )

                BreastCancerSingleLineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Email",
                    value = email,
                    leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = "Email") },
                    onValueChange = { email = it },
                    colors = tfColors,
                    borderColor = borderNormal,
                    focusedBorderColor = borderFocused
                )
                if (!emailValid && email.isNotBlank()) {
                    Text("Please enter a valid email.", color = MaterialTheme.colorScheme.error)
                }
                BreastCancerSingleLineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Password",
                    value = pw,
                    leadingIcon = { Icon(Icons.Outlined.Password, contentDescription = "Password") },
                    onValueChange = { pw = it },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = tfColors,
                    borderColor = borderNormal,
                    focusedBorderColor = borderFocused
                )
                if (pwTooShort) {
                    Text("Password must be at least 6 characters.", color = MaterialTheme.colorScheme.error)
                }

                BreastCancerSingleLineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Confirm password",
                    value = confirm,
                    leadingIcon = { Icon(Icons.Outlined.Password, contentDescription = "Confirm") },
                    onValueChange = { confirm = it },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = tfColors,
                    borderColor = borderNormal,
                    focusedBorderColor = borderFocused
                )

                if (!pwMatch && confirm.isNotBlank()) {
                    Text("Passwords do not match.", color = MaterialTheme.colorScheme.error)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Checkbox(checked = agree, onCheckedChange = { agree = it })
                    Text("I agree to the Terms & Privacy")
                }
            }
        }

        val safeOnClick = { if (canSubmit) onBack() }
        Box(
            modifier = Modifier.width(formWidth),
            contentAlignment = Alignment.Center
        ) {
            BreastCancerButton(
                text = "Create account",
                onClick = safeOnClick
            )
        }

        TextButton(onClick = onBack) {
            Text("Back to login")
        }
    }
}
