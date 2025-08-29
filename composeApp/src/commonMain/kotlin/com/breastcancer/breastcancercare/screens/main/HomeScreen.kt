package com.breastcancer.breastcancercare.screens.main

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.breastcancer.breastcancercare.Res
import com.breastcancer.breastcancercare.components.CoreHomeCardDesign
import com.breastcancer.breastcancercare.default_blog_image
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.utils.DefaultImage
import com.breastcancer.breastcancercare.viewmodel.HomeViewModel
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = koinViewModel(), onBlogClick: () -> Unit) {
    val greetingText by homeViewModel.homeGreeting.collectAsStateWithLifecycle()
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
                    color = MaterialTheme.colorScheme.secondary
                )
            )
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
        item {
            Text(
                modifier = Modifier.padding(horizontal = DefaultHorizontalPaddingLarge),
                text = "Recommended Blogs",
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
                items(count = 5) {
                    BlogCard(onClick = onBlogClick)
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
fun BlogCard(onClick: () -> Unit) =
    CoreHomeCardDesign(
        onClick = onClick,
        modifier = Modifier.fillMaxHeight().width(280.dp)
            .padding(vertical = DefaultVerticalPaddingMedium), image = {
            DefaultImage(
                modifier = Modifier.fillMaxWidth().height(150.dp),
                resource = Res.drawable.default_blog_image,
                contentScale = ContentScale.Crop
            )
        }, title = {
            Text(
                text = "Blog Title",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
        }, subtitle = {
            Text(
                text = "Blog description",
                style = MaterialTheme.typography.labelMedium
            )
        })