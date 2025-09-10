package com.breastcancer.breastcancercare.screens.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.components.BreastCancerCircularLoader
import com.breastcancer.breastcancercare.components.CategoriesLabelSection
import com.breastcancer.breastcancercare.components.CoreHomeCardDesign
import com.breastcancer.breastcancercare.components.DefaultSpacerSize
import com.breastcancer.breastcancercare.components.LazyColumnCollapsibleHeader
import com.breastcancer.breastcancercare.components.TimeAndDateFormat
import com.breastcancer.breastcancercare.components.UrlImage
import com.breastcancer.breastcancercare.models.BlogDTO
import com.breastcancer.breastcancercare.models.ActivityDTO
import com.breastcancer.breastcancercare.states.HomeUIState
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultTopHeaderTextSize
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingSmall
import com.breastcancer.breastcancercare.theme.OffBackground
import com.breastcancer.breastcancercare.theme.spToDp
import com.breastcancer.breastcancercare.utils.emojiFor
import com.breastcancer.breastcancercare.viewmodel.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = koinViewModel(),
    onBlogClick: (blog: BlogDTO) -> Unit,
    onAllBlogs: () -> Unit
) {
    val greetingText by homeViewModel.homeGreeting.collectAsStateWithLifecycle()
    val recommendedBlogsUIState by homeViewModel.recommendedBlogsUIState.collectAsStateWithLifecycle()
    val upcomingEventsUIState by homeViewModel.upcomingEventsUIState.collectAsStateWithLifecycle()
    val overscrollEffect = rememberOverscrollEffect()
    LazyColumnCollapsibleHeader(
        modifier = Modifier
            .fillMaxSize()
            .background(OffBackground)
            .overscroll(overscrollEffect = overscrollEffect),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        headerHeight = spToDp(DefaultTopHeaderTextSize) * 3f,
        header = {
            val builtText = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = DefaultTopHeaderTextSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                ) {
                    append(greetingText)
                }
                withStyle(style = SpanStyle(fontSize = DefaultTopHeaderTextSize / 1.5f)) {
                    append(" ${emojiFor()}")
                }
            }
            Text(
                modifier = Modifier.padding(
                    vertical = DefaultVerticalPaddingMedium
                ),
                text = builtText,
                lineHeight = DefaultTopHeaderTextSize * 1.0f
            )
        }
    ) {
        item {
            AnimatedContent(
                modifier = Modifier.fillMaxWidth(),
                targetState = recommendedBlogsUIState
            ) { state ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingMedium),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (state is HomeUIState.Success || state is HomeUIState.Loading)
                        HeaderAndShowAll(headerText = "Recommended Blogs", onShowAll = onAllBlogs)
                    when (state) {
                        is HomeUIState.Loading, is HomeUIState.Initial -> BreastCancerCircularLoader()
                        is HomeUIState.Success -> {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(330.dp).overscroll(overscrollEffect = overscrollEffect),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(horizontal = DefaultHorizontalPaddingLarge),
                            ) {
                                items(items = state.data ?: emptyList()) { blog ->
                                    BlogCard(blog = blog, onClick = onBlogClick)
                                }
                            }
                        }

                        else -> Unit
                    }
                }
            }
        }
        item {
            AnimatedContent(
                modifier = Modifier.fillMaxWidth(),
                targetState = upcomingEventsUIState
            ) { state ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingMedium),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (state is HomeUIState.Success || state is HomeUIState.Loading)
                        HeaderAndShowAll(headerText = "Upcoming Events", onShowAll = {})
                    when (state) {
                        is HomeUIState.Loading, is HomeUIState.Initial -> BreastCancerCircularLoader()
                        is HomeUIState.Success -> {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp).overscroll(overscrollEffect = overscrollEffect),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(horizontal = DefaultHorizontalPaddingLarge),
                            ) {
                                items(items = state.data ?: emptyList()) { event ->
                                    EventCard(event = event, onClick = {})
                                }
                            }
                        }

                        else -> Unit
                    }
                }
            }
        }
        item {
            DefaultSpacerSize()
        }
    }
}

@Composable
fun EventCard(event: ActivityDTO, onClick: (event: ActivityDTO) -> Unit) =
    CoreHomeCardDesign(
        onClick = { onClick(event) },
        modifier = Modifier.fillMaxHeight().width(300.dp)
            .padding(vertical = DefaultVerticalPaddingMedium), image = {
            UrlImage(
                modifier = Modifier.fillMaxWidth().height(150.dp),
                url = event.image ?: "",
            )
        }, title = {
            Column(verticalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingSmall)) {
                TimeAndDateFormat(activityDTO = event, selectedDate = event.endDate)
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }, subtitle = {
            Text(
                text = event.description,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        })

@Composable
private fun BlogCard(blog: BlogDTO, onClick: (blog: BlogDTO) -> Unit) =
    CoreHomeCardDesign(
        onClick = { onClick(blog) },
        modifier = Modifier.fillMaxHeight().width(300.dp)
            .padding(vertical = DefaultVerticalPaddingMedium), image = {
            UrlImage(
                modifier = Modifier.fillMaxWidth().height(150.dp),
                url = blog.image,
            )
        }, title = {
            Text(
                text = blog.title,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
        }, subtitle = {
            Text(
                text = blog.body,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }, categories = {
            CategoriesLabelSection(blog.categories)
        })

@Composable
private fun HeaderAndShowAll(headerText: String, onShowAll: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = DefaultHorizontalPaddingLarge),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            modifier = Modifier,
            text = headerText,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
            )
        )
        Text(
            modifier = Modifier.clip(MaterialTheme.shapes.extraSmall)
                .clickable(onClick = onShowAll),
            text = "Show All",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Normal,
                textDecoration = TextDecoration.Underline
            )
        )
    }
}