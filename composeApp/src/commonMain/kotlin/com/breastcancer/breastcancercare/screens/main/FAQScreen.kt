@file:OptIn(
    androidx.compose.material3.ExperimentalMaterial3Api::class,
    androidx.compose.foundation.ExperimentalFoundationApi::class
)

package com.breastcancer.breastcancercare.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.components.LazyColumnCollapsibleHeader
import com.breastcancer.breastcancercare.components.icons.Keyboard_arrow_down
import com.breastcancer.breastcancercare.components.loader.LoaderState
import com.breastcancer.breastcancercare.components.snackbar.SnackBarLengthMedium
import com.breastcancer.breastcancercare.components.snackbar.SnackBarState
import com.breastcancer.breastcancercare.models.FAQDTO
import com.breastcancer.breastcancercare.models.GuideDTO
import com.breastcancer.breastcancercare.states.FAQUIState
import com.breastcancer.breastcancercare.theme.DefaultElevation
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultSpacerSize
import com.breastcancer.breastcancercare.theme.DefaultTopHeaderTextSize
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingSmall
import com.breastcancer.breastcancercare.theme.InfoAnim
import com.breastcancer.breastcancercare.theme.InfoColors
import com.breastcancer.breastcancercare.theme.InfoDimens
import com.breastcancer.breastcancercare.utils.DefaultSpacer
import com.breastcancer.breastcancercare.utils.rememberIsLandscape
import com.breastcancer.breastcancercare.viewmodel.FAQViewModel
import org.koin.compose.viewmodel.koinViewModel


private enum class InfoTab { FAQs, Guides }

private fun formatMeta(readTimeMin: Int, updatedAtLabel: String): String {
    return "${readTimeMin} min read · Updated $updatedAtLabel"
}


@Composable
private fun highlightQuery(text: String, query: String): androidx.compose.ui.text.AnnotatedString {
    if (query.isBlank()) return androidx.compose.ui.text.AnnotatedString(text)
    val lower = text.lowercase()
    val q = query.lowercase()
    val builder = buildAnnotatedString {
        var start = 0
        while (true) {
            val idx = lower.indexOf(q, startIndex = start)
            if (idx < 0) {
                append(text.substring(start))
                break
            }
            append(text.substring(start, idx))
            withStyle(
                SpanStyle(
                    fontWeight = FontWeight.Bold,
                    textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                )
            ) {
                append(text.substring(idx, idx + q.length))
            }
            start = idx + q.length
        }
    }
    return builder
}


@Composable
fun FAQScreen(
    loaderState: LoaderState,
    bottomSpacer: Dp = DefaultSpacerSize,
    snackBarState: SnackBarState,
    viewModel: FAQViewModel = koinViewModel()
) {
    val uiState by viewModel.faqUIState.collectAsStateWithLifecycle()

    var currentTab by rememberSaveable { mutableStateOf(InfoTab.FAQs) }

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    val displayedFaqs by viewModel.displayedFaqs.collectAsStateWithLifecycle()
    val displayedGuides by viewModel.displayedGuides.collectAsStateWithLifecycle()

    val isLandscape = rememberIsLandscape()



    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is FAQUIState.Loading -> loaderState.show()
            is FAQUIState.Success -> loaderState.hide()
            is FAQUIState.Error -> {
                loaderState.hide()
                snackBarState.show(
                    overridingText = state.message,
                    overridingDelay = SnackBarLengthMedium
                )
            }

            else -> Unit
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = InfoAnim.Expand,
                    easing = LinearEasing
                )
            ),
        verticalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingMedium),
    ) {
        stickyHeader {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DefaultHorizontalPaddingMedium),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth().background(color = MaterialTheme.colorScheme.background)
                        .padding(
                            vertical = DefaultVerticalPaddingSmall,
                        ),
                    text = "Info",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = DefaultTopHeaderTextSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                TabRow(selectedTabIndex = currentTab.ordinal) {
                    Tab(
                        selected = currentTab == InfoTab.FAQs,
                        onClick = { currentTab = InfoTab.FAQs },
                        text = { Text("FAQs") }
                    )
                    Tab(
                        selected = currentTab == InfoTab.Guides,
                        onClick = { currentTab = InfoTab.Guides },
                        text = { Text("Guides") }
                    )
                }

                // Search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchChange(it) },
                    singleLine = true,
                    label = { Text("Search topics") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { viewModel.onSearchChange("") }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Clear"
                                )
                            }
                        }
                    },
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier.fillMaxWidth().background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                            )
                        )
                    ).padding(vertical = DefaultVerticalPaddingSmall)
                )

            }
        }
        when (currentTab) {
            InfoTab.FAQs -> {
                if (displayedFaqs.isEmpty() && searchQuery.isNotEmpty()) {
                    item {
                        Text(
                            modifier = Modifier.padding(
                                horizontal = InfoDimens.ScreenHPadding,
                                vertical = InfoDimens.ScreenVPadding
                            ),
                            text = "No Search Results for \"${searchQuery}\"",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    itemsIndexed(items = displayedFaqs) { index, item ->
                        FAQCard(index = index, item = item, searchQuery = searchQuery)
                    }
                }
            }

            InfoTab.Guides -> {
                if (displayedGuides.isEmpty()) {
                    item {
                        Text(
                            modifier = Modifier.padding(
                                horizontal = InfoDimens.ScreenHPadding,
                                vertical = InfoDimens.ScreenVPadding
                            ),
                            text = "No guides for the current search.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    itemsIndexed(displayedGuides) { _, g ->
                        GuideCard(item = g)
                    }
                }
            }
        }
        item {
            DefaultSpacer(bottomSpacer)
        }
    }
}

@Composable
private fun FAQCard(index: Int, item: FAQDTO, searchQuery: String) {
    var isExpanded by rememberSaveable(item.question) { mutableStateOf(false) }
    val angle: Float by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(
            durationMillis = InfoAnim.Expand,
            easing = LinearEasing
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DefaultHorizontalPaddingMedium)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = InfoAnim.Expand,
                    easing = LinearEasing
                )
            ),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(DefaultElevation),
        onClick = { isExpanded = !isExpanded }
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = DefaultHorizontalPaddingMedium,
                vertical = DefaultVerticalPaddingSmall
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    modifier = Modifier.rotate(angle),
                    imageVector = Keyboard_arrow_down,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = highlightQuery(item.question, searchQuery),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            AnimatedVisibility(visible = isExpanded) {
                Text(
                    modifier = Modifier.padding(
                        vertical = DefaultVerticalPaddingSmall,
                        horizontal = DefaultHorizontalPaddingMedium
                    ),
                    text = item.answer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun GuideCard(item: GuideDTO) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DefaultHorizontalPaddingMedium),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = DefaultElevation)
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = InfoDimens.ScreenHPadding,
                vertical = InfoDimens.ScreenVPadding
            )
        ) {
            AssistChip(
                onClick = { /* no-op */ },
                label = { Text(item.category) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )

            Spacer(modifier = Modifier.height(InfoDimens.ScreenVPadding / 2))

            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(InfoDimens.ScreenVPadding / 3))

            Text(
                text = item.summary,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(InfoDimens.ScreenVPadding / 2))

            Text(
                text = formatMeta(item.readTimeMin, item.updatedAtLabel),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
