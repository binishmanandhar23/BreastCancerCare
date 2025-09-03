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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.components.loader.LoaderState
import com.breastcancer.breastcancercare.components.snackbar.SnackBarLengthMedium
import com.breastcancer.breastcancercare.components.snackbar.SnackBarState
import com.breastcancer.breastcancercare.models.GuideDTO
import com.breastcancer.breastcancercare.states.FAQUIState
import com.breastcancer.breastcancercare.theme.DefaultElevation
import com.breastcancer.breastcancercare.theme.InfoAnim
import com.breastcancer.breastcancercare.theme.InfoColors
import com.breastcancer.breastcancercare.theme.InfoDimens
import com.breastcancer.breastcancercare.viewmodel.FAQViewModel
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.RadioButton
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.clickable



private enum class InfoTab { FAQs, Guides }

private fun formatMeta(readTimeMin: Int, updatedAtLabel: String): String {
    return "$readTimeMin min read · Updated $updatedAtLabel"
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
            withStyle(SpanStyle(fontWeight = FontWeight.Bold, textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline)) {
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
    snackBarState: SnackBarState,
    viewModel: FAQViewModel = koinViewModel(),
    onGuideClick: (GuideDTO) -> Unit
) {
    val uiState by viewModel.faqUIState.collectAsStateWithLifecycle()
    val suitabilities by viewModel.suitabilities.collectAsStateWithLifecycle()

    var showFilter by remember { mutableStateOf(false) }
    var currentTab by rememberSaveable { mutableStateOf(InfoTab.FAQs) }

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedKey by viewModel.selectedSuitabilityKey.collectAsStateWithLifecycle()
    val selectedLabel = remember(selectedKey, suitabilities) {
        val k = selectedKey
        if (k == null) "All" else suitabilities.firstOrNull { it.key == k }?.name ?: "All"
    }
    val displayedFaqs by viewModel.displayedFaqs.collectAsStateWithLifecycle()
    val displayedGuides by viewModel.displayedGuides.collectAsStateWithLifecycle()
    var expandedIndex by rememberSaveable { mutableStateOf<Int?>(null) }



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
    val listState = rememberLazyListState()
    val elevated by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0 }
    }
    val headerElevation = if (elevated) DefaultElevation else 0.dp

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize(animationSpec = tween(durationMillis = InfoAnim.Expand, easing = LinearEasing)),
        verticalArrangement = Arrangement.spacedBy(InfoDimens.CardSpacing),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            bottom = InfoDimens.ScreenVPadding
        )
    ) {
        stickyHeader {
            Surface(
                color = MaterialTheme.colorScheme.background,
                shadowElevation = headerElevation,
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = InfoDimens.ScreenHPadding)
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = InfoDimens.ScreenVPadding),
                        text = "Info",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
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

                    Spacer(modifier = Modifier.height(InfoDimens.ScreenVPadding / 2))
                    val focusManager = LocalFocusManager.current
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
                            Row {
                                if (searchQuery.isNotBlank()) {
                                    IconButton(onClick = { viewModel.onSearchChange("") }) {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = "Clear"
                                        )
                                    }
                                }

                                val currentFilterLabel = if (selectedKey == null) "All" else selectedLabel

                                IconButton(onClick = { showFilter = true }) {
                                    Icon(
                                        imageVector = Icons.Filled.FilterList,
                                        contentDescription = "Open filter. Current: $currentFilterLabel"
                                    )
                                }
                            }
                        }
                        ,
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("SearchField")
                        ,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                    )
                    Spacer(modifier = Modifier.height(InfoDimens.ScreenVPadding / 2))

                    if (showFilter) {
                        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                        ModalBottomSheet(
                            onDismissRequest = { showFilter = false },
                            sheetState = sheetState
                        ) {

                            Text(
                                text = when (currentTab) {
                                    InfoTab.FAQs -> "Suitability"
                                    InfoTab.Guides -> "Suitability"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .padding(horizontal = InfoDimens.ScreenHPadding, vertical = 8.dp)
                                    .semantics { heading() }
                            )
                            Spacer(Modifier.height(8.dp))


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = InfoDimens.ScreenHPadding, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (selectedKey == null),
                                    onClick = {
                                        viewModel.onSuitabilityChange(null)
                                        showFilter = false
                                    }
                                )
                                Text("All", modifier = Modifier.padding(start = 8.dp))
                            }
                            HorizontalDivider()

                            suitabilities.forEach { s ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = InfoDimens.ScreenHPadding, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    androidx.compose.material3.RadioButton(
                                        selected = (selectedKey == s.key),
                                        onClick = {
                                            viewModel.onSuitabilityChange(s.key)
                                            showFilter = false
                                        }
                                    )
                                    Text(s.name, modifier = Modifier.padding(start = 8.dp))
                                }
                                HorizontalDivider()
                            }

                            Spacer(Modifier.height(16.dp))
                        }
                    }

                }
            }
        }
            when (currentTab) {
                InfoTab.FAQs -> {
                    if (displayedFaqs.isEmpty()) {
                        item {
                            Text(
                                modifier = Modifier.padding(
                                    horizontal = InfoDimens.ScreenHPadding,
                                    vertical = InfoDimens.ScreenVPadding
                                ),
                                text = "No FAQs for the current filter.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        itemsIndexed(items = displayedFaqs) { index, item ->
                            val isExpanded = expandedIndex == index
                            FaqCard(
                                index = index,
                                question = item.question,
                                answer = item.answer,
                                query = searchQuery,
                                isExpanded = isExpanded,
                                onToggle = {
                                    expandedIndex = if (isExpanded) null else index
                                }
                            )
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
                            GuideCard(
                                item = g,
                                onClick = { clicked -> onGuideClick(clicked) }
                            )
                        }
                    }
                }
            }
        }
    }

@Composable
private fun GuideCard(
    item: GuideDTO,
    onClick: (GuideDTO) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = InfoDimens.ScreenHPadding)
            .animateContentSize(animationSpec = tween(durationMillis = InfoAnim.Expand, easing = LinearEasing))
            .clickable { onClick(item) },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(DefaultElevation)
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


@Composable
private fun FaqCard(
    index: Int,
    question: String,
    answer: String,
    query: String,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    val containerColor = InfoColors.faqCard(index)
    val onColor = InfoColors.onFaqCard()
    val angle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = InfoAnim.Expand, easing = LinearEasing)
    )
    val bringIntoViewRequester = remember { BringIntoViewRequester() }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = InfoDimens.ScreenHPadding)
            .animateContentSize(animationSpec = tween(durationMillis = InfoAnim.Expand, easing = LinearEasing))
            .then(Modifier.bringIntoViewRequester(bringIntoViewRequester)),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = onColor
        ),
        elevation = CardDefaults.cardElevation(DefaultElevation),
        onClick = onToggle
    ) {
        LaunchedEffect(isExpanded) {
            if (isExpanded) bringIntoViewRequester.bringIntoView()
        }
        Column(
            modifier = Modifier
                .padding(horizontal = InfoDimens.ScreenHPadding, vertical = InfoDimens.ScreenVPadding)
                .semantics(mergeDescendants = true) {
                    heading()
                    role = androidx.compose.ui.semantics.Role.Button
                    stateDescription = if (isExpanded) "Expanded" else "Collapsed"
                    contentDescription = "FAQ item"
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .rotate(angle)
                        .padding(top = 2.dp),
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand"
                )
                Text(
                    text = highlightQuery(question, query),
                    color = onColor,
                    style = LocalTextStyle.current.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        lineHeight = 26.sp
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = onColor.copy(alpha = 0.2f)
                    )
                    Text(
                        text = answer,
                        color = onColor,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp)
                    )
                }
            }
        }
    }
}

