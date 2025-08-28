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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
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
import com.breastcancer.breastcancercare.viewmodel.FAQViewModel
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import com.breastcancer.breastcancercare.theme.InfoDimens
import com.breastcancer.breastcancercare.theme.InfoColors
import com.breastcancer.breastcancercare.theme.InfoAnim
import com.breastcancer.breastcancercare.theme.DefaultElevation
import androidx.compose.material3.Surface
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.zIndex
import androidx.compose.material3.TabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle


private enum class InfoTab { FAQs, Guides }

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
    viewModel: FAQViewModel = koinViewModel()
) {
    val uiState by viewModel.faqUIState.collectAsStateWithLifecycle()
    val suitabilities by viewModel.suitabilities.collectAsStateWithLifecycle()
    var selectedKey by rememberSaveable { mutableStateOf<String?>(null) }
    var menuExpanded by remember { mutableStateOf(false) }
    var currentTab by rememberSaveable { mutableStateOf(InfoTab.FAQs) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val selectedLabel = remember(selectedKey, suitabilities) {
        val k = selectedKey
        if (k == null) "All" else suitabilities.firstOrNull { it.key == k }?.name ?: "All"
    }
    var faqs: List<FAQDTO>? by remember { mutableStateOf(null) }
    val displayedFaqs = remember(faqs, selectedKey, searchQuery) {
        val base = faqs ?: emptyList()
        val bySuit = if (selectedKey.isNullOrEmpty()) base
        else base.filter { faq -> faq.suitabilities.any { it.key == selectedKey } }

        if (searchQuery.isBlank()) bySuit
        else {
            val q = searchQuery.trim().lowercase()
            bySuit.filter { it.question.lowercase().contains(q) || it.answer.lowercase().contains(q) }
        }
    }

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

                    // Search bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
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
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Clear"
                                    )
                                }
                            }
                        },
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(InfoDimens.ScreenVPadding / 2))

                    if (currentTab == InfoTab.FAQs) {
                        ExposedDropdownMenuBox(
                            expanded = menuExpanded,
                            onExpandedChange = { menuExpanded = !menuExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = if (selectedKey == null) "All" else selectedLabel,
                                onValueChange = {},
                                readOnly = true,
                                singleLine = true,
                                label = { Text("Suitability") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpanded) },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                shape = MaterialTheme.shapes.large,
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("All") },
                                    onClick = {
                                        selectedKey = null
                                        menuExpanded = false
                                    }
                                )
                                suitabilities.forEach { s ->
                                    DropdownMenuItem(
                                        text = { Text(s.name) },
                                        onClick = {
                                            selectedKey = s.key
                                            menuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(InfoDimens.ScreenVPadding))
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
                        itemsIndexed(items = displayedFaqs) { key, item ->
                            val color = InfoColors.faqCard(key)
                            val onColor = InfoColors.onFaqCard()
                            var isExpanded by rememberSaveable(item.question) { mutableStateOf(false) }
                            val angle: Float by animateFloatAsState(
                                targetValue = if (isExpanded) 180f else 0f,
                                animationSpec = tween(durationMillis = InfoAnim.Expand, easing = LinearEasing)
                            )

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = InfoDimens.ScreenHPadding)
                                    .animateContentSize(animationSpec = tween(durationMillis = InfoAnim.Expand, easing = LinearEasing)),
                                shape = MaterialTheme.shapes.large,
                                colors = CardDefaults.cardColors(
                                    containerColor = color,
                                    contentColor = onColor
                                ),
                                elevation = CardDefaults.cardElevation(DefaultElevation),
                                onClick = { isExpanded = !isExpanded }
                            ) {
                                Column(
                                    modifier = Modifier.padding(
                                        horizontal = InfoDimens.ScreenHPadding,
                                        vertical = InfoDimens.ScreenVPadding
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
                                            text = highlightQuery(item.question, searchQuery),
                                            color = onColor,
                                            style = LocalTextStyle.current.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 20.sp
                                            )
                                        )
                                    }
                                    AnimatedVisibility(visible = isExpanded) {
                                        Text(
                                            modifier = Modifier.padding(vertical = InfoDimens.ScreenVPadding),
                                            text = item.answer,
                                            color = onColor
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                InfoTab.Guides -> {
                    item {
                        Text(
                            modifier = Modifier.padding(
                                horizontal = InfoDimens.ScreenHPadding,
                                vertical = InfoDimens.ScreenVPadding
                            ),
                            text = "Guides are coming soon.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }