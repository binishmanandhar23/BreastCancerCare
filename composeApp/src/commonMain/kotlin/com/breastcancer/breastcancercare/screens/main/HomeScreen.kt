package com.breastcancer.breastcancercare.screens.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.Res
import com.breastcancer.breastcancercare.components.BreastCancerCircularLoader
import com.breastcancer.breastcancercare.components.CategoriesLabelSection
import com.breastcancer.breastcancercare.components.CategoryChip
import com.breastcancer.breastcancercare.components.CoreHomeCardDesign
import com.breastcancer.breastcancercare.components.UrlImage
import com.breastcancer.breastcancercare.default_blog_image
import com.breastcancer.breastcancercare.models.BlogDTO
import com.breastcancer.breastcancercare.states.HomeUIState
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.utils.DefaultImage
import com.breastcancer.breastcancercare.viewmodel.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = koinViewModel(),
    onBlogClick: (blog: BlogDTO) -> Unit
) {
    val greetingText by homeViewModel.homeGreeting.collectAsStateWithLifecycle()
    val recommendedBlogsUIState by homeViewModel.recommendedBlogUIState.collectAsStateWithLifecycle()
    val overscrollEffect = rememberOverscrollEffect()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .animateContentSize().overscroll(overscrollEffect = overscrollEffect),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                modifier = Modifier.padding(
                    horizontal = DefaultHorizontalPaddingLarge,
                    vertical = DefaultVerticalPaddingMedium
                ),
                text = greetingText,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
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
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = DefaultHorizontalPaddingLarge),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                modifier = Modifier,
                                text = "Recommended Blogs",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                )
                            )
                            Text(
                                modifier = Modifier.clip(MaterialTheme.shapes.extraSmall)
                                    .clickable {

                                    },
                                text = "Show All",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Normal,
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                        }
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
            Text(
                modifier = Modifier.padding(horizontal = DefaultHorizontalPaddingLarge),
                text = "Suggested Programs",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                )
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp).overscroll(overscrollEffect = overscrollEffect),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = DefaultHorizontalPaddingLarge),
            ) {
                items(count = 10) {
                    ProgramCard()
                }
            }
        }
    }
}

@Composable
fun ProgramCard() =
    CoreHomeCardDesign(
        onClick = {},
        modifier = Modifier.fillMaxHeight().width(280.dp)
            .padding(vertical = DefaultVerticalPaddingMedium), title = {
            Text(
                text = "Yoga Class",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
        }, subtitle = {
            Text(
                text = "Aug 18 - 10:00 am",
                style = MaterialTheme.typography.labelMedium
            )
        })

@Composable
fun BlogCard(blog: BlogDTO, onClick: (blog: BlogDTO) -> Unit) =
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