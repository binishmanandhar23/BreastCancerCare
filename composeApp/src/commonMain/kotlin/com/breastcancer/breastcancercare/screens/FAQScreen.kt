package com.breastcancer.breastcancercare.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.components.loader.LoaderState
import com.breastcancer.breastcancercare.components.snackbar.SnackBarLengthMedium
import com.breastcancer.breastcancercare.components.snackbar.SnackBarState
import com.breastcancer.breastcancercare.models.FAQDTO
import com.breastcancer.breastcancercare.states.FAQUIState
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPadding
import com.breastcancer.breastcancercare.theme.DefaultVerticalPadding
import com.breastcancer.breastcancercare.viewmodel.FAQViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FAQScreen(
    loaderState: LoaderState,
    snackBarState: SnackBarState,
    viewModel: FAQViewModel = koinViewModel()
) {
    val uiState by viewModel.faqUIState.collectAsStateWithLifecycle()

    var faqs: List<FAQDTO>? by remember { mutableStateOf(null) }

    LaunchedEffect(key1 = uiState) {
        when (val state = uiState) {
            is FAQUIState.Loading -> loaderState.show()
            is FAQUIState.Success -> {
                faqs = state.data
                loaderState.hide()
            }

            else -> snackBarState.show(
                overridingText = state?.message,
                overridingDelay = SnackBarLengthMedium
            ).also { loaderState.hide() }
        }
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize().animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(DefaultVerticalPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(items = faqs ?: emptyList()) { key, item ->
            val color = if (key % 2 == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = DefaultHorizontalPadding),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = color),
            ) {
                Column(modifier = Modifier.padding(horizontal = DefaultHorizontalPadding, vertical = DefaultVerticalPadding)) {
                    Text(item.question, color = MaterialTheme.colorScheme.onPrimary)
                    Text(item.answer, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}