package com.breastcancer.breastcancercare.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.breastcancer.breastcancercare.components.BreastCancerAlertDialog
import com.breastcancer.breastcancercare.components.snackbar.SnackBarLengthMedium
import com.breastcancer.breastcancercare.components.snackbar.SnackBarState
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingSmall
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import dev.icerock.moko.permissions.PermissionState
import androidx.compose.ui.Alignment


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    permissionState: PermissionState,
    customSnackBarState: SnackBarState,
    onOpenProfile: () -> Unit,
    onOpenAbout: () -> Unit,
    onContactSupport: () -> Unit,
    onLogOut: () -> Unit
) {
    var notificationsEnabled by rememberSaveable { mutableStateOf(false) }
    var showFeedbackDialog by rememberSaveable { mutableStateOf(false) }
    var feedbackMessage by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(notificationsEnabled, permissionState) {
        if ((permissionState == PermissionState.Denied
                || permissionState == PermissionState.DeniedAlways
                || permissionState == PermissionState.NotGranted) && notificationsEnabled
        ) {
            notificationsEnabled = false
            customSnackBarState.show(overridingText = "Please grant notifications permission.", overridingDelay = SnackBarLengthMedium)
        }
    }

    val listOfItems = listOf(
        Pair<String, @Composable (ColumnScope.() -> Unit)>(
            "Account",
            {
                NavRow(text = "Profile", onClick = onOpenProfile)
            },
        ),
        Pair<String, @Composable (ColumnScope.() -> Unit)>(
            "Notifications",
            {
                SwitchRow(
                    text = "Notifications",
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
            },
        ),
        Pair<String, @Composable (ColumnScope.() -> Unit)>(
            "Feedback",
            {
                NavRow(text = "Feedback", onClick = { showFeedbackDialog = true })
            },
        ),
        Pair<String, @Composable (ColumnScope.() -> Unit)>(
            "Help",
            {
                NavRow(text = "About", onClick = onOpenAbout)
                NavRow(text = "Contact support", onClick = onContactSupport)
            },
        ),
        Pair<String, @Composable (ColumnScope.() -> Unit)>(
            "",
            {
                NavRow(text = "Log Out", onClick = onLogOut)
            },
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = DefaultVerticalPaddingMedium, horizontal = DefaultHorizontalPaddingSmall),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        stickyHeader {
            Text(
                "Settings",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
        items(listOfItems) { pair ->
            SettingsSection(title = pair.first, content = pair.second)
        }
    }

    if (showFeedbackDialog)
        BreastCancerAlertDialog(
            title = "Send Feedback",
            confirmText = "Send",
            dismissText = "Cancel",
            text = {
                OutlinedTextField(
                    value = feedbackMessage,
                    onValueChange = { feedbackMessage = it },
                    placeholder = { Text("Type your feedback...") },
                    minLines = 4
                )
            },
            onConfirm = {
                feedbackMessage = ""
                showFeedbackDialog = false
            },
            onDismissRequest = {
                feedbackMessage = ""
                showFeedbackDialog = false
            })
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
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
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
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.alignBy { it.measuredHeight / 2 }) {
            IconPlaceholder()
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .alignBy { it.measuredHeight / 2 }
        )

        Switch(checked = checked, onCheckedChange = onCheckedChange, modifier = Modifier.alignBy { it.measuredHeight / 2 })
    }
    HorizontalDivider(thickness = 0.5.dp)
}