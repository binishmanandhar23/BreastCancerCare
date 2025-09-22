package com.breastcancer.breastcancercare.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.breastcancer.breastcancercare.components.BreastCancerAlertDialog
import com.breastcancer.breastcancercare.components.icons.Contact_support
import com.breastcancer.breastcancercare.components.icons.InfoSquare
import com.breastcancer.breastcancercare.components.icons.Keyboard_arrow_right
import com.breastcancer.breastcancercare.components.icons.Logout
import com.breastcancer.breastcancercare.components.icons.Switch_account
import com.breastcancer.breastcancercare.components.snackbar.SnackBarLengthMedium
import com.breastcancer.breastcancercare.components.snackbar.SnackBarState
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingSmall
import com.breastcancer.breastcancercare.theme.DefaultSpacerSize
import com.breastcancer.breastcancercare.theme.DefaultTopHeaderTextSize
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.utils.DefaultSpacer
import dev.icerock.moko.permissions.PermissionState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    permissionState: PermissionState,
    customSnackBarState: SnackBarState,
    bottomSpacer: Dp = DefaultSpacerSize,
    onOpenProfile: () -> Unit,
    onOpenAbout: () -> Unit,
    onSwitchJourney: () -> Unit,
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
            customSnackBarState.show(
                overridingText = "Please grant notifications permission.",
                overridingDelay = SnackBarLengthMedium
            )
        }
    }

    val listOfItems = listOf(
        Pair<String, @Composable (ColumnScope.() -> Unit)>(
            "Account",
            {
                NavRow(
                    text = "Profile",
                    icon = Icons.Default.SupervisedUserCircle,
                    onClick = onOpenProfile
                )
            },
        ),
        Pair<String, @Composable (ColumnScope.() -> Unit)>(
            "Account",
            {
                NavRow(
                    text = "Switch Journey",
                    icon = Switch_account,
                    onClick = onSwitchJourney
                )
            },
        ),
        Pair<String, @Composable (ColumnScope.() -> Unit)>(
            "Notifications",
            {
                SwitchRow(
                    text = "Notifications",
                    icon = Icons.Default.Notifications,
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
            },
        ),
        Pair<String, @Composable (ColumnScope.() -> Unit)>(
            "Feedback",
            {
                NavRow(
                    text = "Feedback",
                    icon = Icons.Default.Feedback,
                    onClick = { showFeedbackDialog = true })
            },
        ),
        Pair<String, @Composable (ColumnScope.() -> Unit)>(
            "Help",
            {
                NavRow(text = "About",icon = InfoSquare, onClick = onOpenAbout)
                NavRow(text = "Contact support", icon = Contact_support,onClick = onContactSupport)
            },
        ),
        Pair<String, @Composable (ColumnScope.() -> Unit)>(
            "",
            {
                NavRow(text = "Log Out", icon = Logout, onClick = onLogOut)
            },
        )
    ).groupBy { it.first }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = DefaultVerticalPaddingMedium,
                horizontal = DefaultHorizontalPaddingSmall
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        stickyHeader {
            Text(
                modifier = Modifier.fillMaxWidth().background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                        )
                    )
                ),
                text = "Settings",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = DefaultTopHeaderTextSize,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
        listOfItems.forEach { (title, items) ->
            item {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 16.dp,
                        bottom = 8.dp
                    )
                )
            }
            items(items = items) { content ->
                SettingsSection(content = content.second)
            }
        }
        item {
            DefaultSpacer(bottomSpacer)
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
    content: @Composable ColumnScope.() -> Unit
) {
    Column { content() }
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun IconPlaceholder(icon: ImageVector, contentDescription: String? = null, size: Int = 24) {
    Icon(
        modifier = Modifier
            .size(size.dp),
        imageVector = icon,
        contentDescription = contentDescription
    )
}

@Composable
private fun TrailingPlaceholder(contentDescription: String? = null) {
    Icon(
        modifier = Modifier,
        imageVector = Keyboard_arrow_right,
        contentDescription = contentDescription,
        tint = MaterialTheme.colorScheme.primary
    )
}


@Composable
private fun NavRow(
    text: String,
    icon: ImageVector,
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
        IconPlaceholder(icon = icon)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        TrailingPlaceholder(contentDescription = text)
    }
    HorizontalDivider(thickness = 0.5.dp)
}


@Suppress("SameParameterValue")
@Composable
private fun SwitchRow(
    text: String,
    icon: ImageVector,
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
            IconPlaceholder(icon = icon, contentDescription = text)
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .alignBy { it.measuredHeight / 2 }
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.alignBy { it.measuredHeight / 2 })
    }
    HorizontalDivider(thickness = 0.5.dp)
}