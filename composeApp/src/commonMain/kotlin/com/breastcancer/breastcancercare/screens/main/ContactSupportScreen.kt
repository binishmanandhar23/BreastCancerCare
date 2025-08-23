@file:OptIn(ExperimentalMaterial3Api::class)

package com.breastcancer.breastcancercare.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api

@Composable
fun ContactSupportScreen(onBack: () -> Unit = {}) {
    val uriHandler = LocalUriHandler.current
    val supportEmail = "support@example.com" // TODO: replace with real support email
    val supportPhone = "+61 8 1234 5678"     // TODO: replace with real support phone (E.164 recommended)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contact support") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
                    }
                }
            )
        }
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .padding(inner)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Intro
            item {
                Text(
                    "If you have issues or feedback, reach out to us.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Email
            item { Text("Email", style = MaterialTheme.typography.titleMedium) }
            item { Text(supportEmail, style = MaterialTheme.typography.bodyLarge) }
            item {
                Button(
                    onClick = {
                        val subject = "App support"
                        val body = "Please describe your issue here."
                        val mailto = "mailto:$supportEmail?subject=${subject.urlEncode()}&body=${body.urlEncode()}"
                        uriHandler.openUri(mailto)
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) { Text("Email support") }
            }

            // Phone
            item { Text("Phone", style = MaterialTheme.typography.titleMedium) }
            item { Text(supportPhone, style = MaterialTheme.typography.bodyLarge) }
            item {
                Button(
                    onClick = {
                        val digits = supportPhone.filter { it.isDigit() || it == '+' }
                        val tel = "tel:$digits"
                        runCatching { uriHandler.openUri(tel) }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) { Text("Call support") }
            }
        }
    }
}

private fun String.urlEncode(): String = buildString {
    for (b in this@urlEncode.encodeToByteArray()) {
        val v = b.toInt() and 0xFF
        val ch = v.toChar()
        val safe = (ch in 'A'..'Z') || (ch in 'a'..'z') || (ch in '0'..'9') || ch == '-' || ch == '_' || ch == '.' || ch == '~'
        if (safe) append(ch) else {
            append('%')
            append(v.toString(16).uppercase().padStart(2, '0'))
        }
    }
}
