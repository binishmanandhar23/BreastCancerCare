@file:OptIn(ExperimentalMaterial3Api::class)

package com.breastcancer.breastcancercare.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.breastcancer.breastcancercare.database.local.dao.UserDao
import com.breastcancer.breastcancercare.viewmodel.ProfileViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.breastcancer.breastcancercare.components.BreastCancerToolbar
import com.breastcancer.breastcancercare.components.CategoryChip
import com.breastcancer.breastcancercare.database.local.types.UserCategory
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingSmall
import org.koin.compose.viewmodel.koinViewModel


data class ProfileUiState(
    val name: String? = null,
    val email: String? = null,
    val userCategory: UserCategory = UserCategory.StartingStrong,
    val initials: String? = null,
    val loading: Boolean = false,
    val error: String? = null
)

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    onBack: () -> Unit = {},
    onEditProfile: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().padding(vertical = 0.dp, horizontal = DefaultHorizontalPaddingMedium)) {
        BreastCancerToolbar(title = "Profile", onBack = onBack)
        if (uiState.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Column
        }

        if (uiState.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
            return@Column
        }

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AvatarPlaceholder(
                        initials = (uiState.initials ?: uiState.name?.firstOrNull()?.uppercase()
                        ?: "U")
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(
                            DefaultVerticalPaddingSmall
                        )
                    ) {
                        Text(
                            uiState.name ?: "—",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(uiState.email ?: "—", style = MaterialTheme.typography.bodyMedium)
                        CategoryChip(categoryName = UserCategory.getLabel(uiState.userCategory))
                    }
                }
            }
            item {
                Button(
                    onClick = onEditProfile,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Edit profile")
                }
            }
        }
    }
}

@Composable
private fun AvatarPlaceholder(
    initials: String,
    size: Dp = 72.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Text(
            initials,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ProfileRoute(
    profileViewModel: ProfileViewModel = koinViewModel(),
    onBack: () -> Unit = {},
    onEditProfile: () -> Unit = {}
) {
    val uiState by profileViewModel.state.collectAsState(
        initial = ProfileUiState(loading = true)
    )

    ProfileScreen(
        uiState = uiState,
        onBack = onBack,
        onEditProfile = onEditProfile
    )
}