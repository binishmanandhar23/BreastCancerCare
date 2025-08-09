package com.breastcancer.breastcancercare.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            val color =
                if (key % 2 == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            var isExpanded by remember {
                mutableStateOf(false)
            }
            val angle: Float by animateFloatAsState(
                targetValue = if (isExpanded) 180f else 0f,
                animationSpec = tween(durationMillis = 200, easing = LinearEasing)
            )

            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = DefaultHorizontalPadding)
                    .animateContentSize(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = color),
                onClick = {
                    isExpanded = !isExpanded
                }
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = DefaultHorizontalPadding,
                        vertical = DefaultVerticalPadding
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            modifier = Modifier.rotate(angle),
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = if (isExpanded) "Collapse" else "Expand"
                        )
                        Text(
                            item.question,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = LocalTextStyle.current.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        )
                    }
                    AnimatedVisibility(visible = isExpanded) {
                        Text(
                            modifier = Modifier.padding(vertical = DefaultVerticalPadding),
                            text = item.answer,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}