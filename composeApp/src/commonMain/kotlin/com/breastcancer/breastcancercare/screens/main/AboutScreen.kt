@file:OptIn(ExperimentalMaterial3Api::class)

package com.breastcancer.breastcancercare.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.platform.LocalUriHandler

@Composable
fun AboutScreen(onBack: () -> Unit = {}) {
    val uriHandler = LocalUriHandler.current
    val websiteUrl = "https://www.breastcancer.org.au/"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About") },
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
            item { Text("BreastCancerCare", style = MaterialTheme.typography.titleLarge) }
            item {
                Text(
                    "Version 1.0.0",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            item {
                Text(
                    "Provides education, calendar and support resources for breast cancer care.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            item {
                Button(
                    onClick = {
                        // Open the real website in the default browser (safe even without scheme)
                        val url = websiteUrl.ensureHttpScheme()
                        runCatching { uriHandler.openUri(url) }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) { Text("Visit website") }
            }
        }
    }
}

private fun String.ensureHttpScheme(): String =
    if (startsWith("http://") || startsWith("https://")) this else "https://$this"
