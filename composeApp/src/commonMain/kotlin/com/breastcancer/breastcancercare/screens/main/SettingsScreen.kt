package com.breastcancer.breastcancercare.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onOpenProfile: () -> Unit = {},
    onOpenAbout: () -> Unit = {},
    onContactSupport: () -> Unit = {}
) {
    var notificationsEnabled by rememberSaveable { mutableStateOf(false) }
    var showFeedbackDialog by rememberSaveable { mutableStateOf(false) }
    var feedbackMessage by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Settings") }) }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            Spacer(Modifier.height(8.dp))
            SettingsSection(
                title = "Account",
                content = {
                    NavRow(text = "Profile", onClick = onOpenProfile)
                }
            )

            SettingsSection(
                title = "Notifications",
                content = {
                    SwitchRow(
                        text = "Notifications",
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                }
            )

            SettingsSection(
                title = "Feedback",
                content = {
                    NavRow(text = "Feedback", onClick = { showFeedbackDialog = true })
                }
            )

            SettingsSection(
                title = "Help",
                content = {
                    NavRow(text = "About", onClick = onOpenAbout)
                    NavRow(text = "Contact support", onClick = onContactSupport)
                }
            )
        }

        if (showFeedbackDialog) {
            AlertDialog(
                onDismissRequest = { showFeedbackDialog = false },
                title = { Text("Send Feedback") },
                text = {
                    OutlinedTextField(
                        value = feedbackMessage,
                        onValueChange = { feedbackMessage = it },
                        placeholder = { Text("Type your feedback...") },
                        minLines = 4
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        // TODO: Wire up email/server send logic
                        showFeedbackDialog = false
                        feedbackMessage = ""
                    }) { Text("Send") }
                },
                dismissButton = {
                    TextButton(onClick = { showFeedbackDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 8.dp)
    )
    Column { content() }
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun IconPlaceholder(size: Int = 24) {
    Box(
        Modifier
            .size(size.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    )
}

@Composable
private fun TrailingPlaceholder(size: Int = 16) {
    Box(
        Modifier
            .size(size.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    )
}


@Composable
private fun NavRow(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconPlaceholder()
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        TrailingPlaceholder()
    }
    HorizontalDivider(thickness = 0.5.dp)
}


@Suppress("SameParameterValue")
@Composable
private fun SwitchRow(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconPlaceholder()
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
    HorizontalDivider(thickness = 0.5.dp)
}