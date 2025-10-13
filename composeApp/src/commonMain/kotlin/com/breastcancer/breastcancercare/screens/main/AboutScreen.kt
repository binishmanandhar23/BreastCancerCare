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
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalUriHandler
import com.breastcancer.breastcancercare.BuildKonfig
import com.breastcancer.breastcancercare.components.BreastCancerToolbar
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.utils.ensureHttpScheme

@Composable
fun AboutScreen(onBack: () -> Unit = {}) {
    val uriHandler = LocalUriHandler.current
    val websiteUrl = remember { "https://www.breastcancer.org.au/" }
    Column(modifier = Modifier.fillMaxSize().padding(vertical = 0.dp, horizontal = DefaultHorizontalPaddingMedium)) {
        BreastCancerToolbar(title = "About", onBack = onBack)
        LazyColumn(
            modifier = Modifier
                .padding(
                    vertical = DefaultVerticalPaddingMedium,
                    horizontal = DefaultHorizontalPaddingMedium
                ),
            verticalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingMedium)
        ) {
            item { Text("BreastCancerCare", style = MaterialTheme.typography.titleLarge) }
            item {
                Text(
                    "Version ${BuildKonfig.APP_VERSION_NAME}",
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


