package com.breastcancer.breastcancercare.screens.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.components.CategoriesLabelSection
import com.breastcancer.breastcancercare.components.UrlImage
import com.breastcancer.breastcancercare.components.loader.LoaderState
import com.breastcancer.breastcancercare.states.BlogUIState
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingSmall
import com.breastcancer.breastcancercare.utils.OverlappingZoomHeaderWithParallax
import com.breastcancer.breastcancercare.viewmodel.BlogViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BlogDetailScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    blogViewModel: BlogViewModel,
    loaderState: LoaderState,
    slug: String,
    onBack: () -> Unit
) {
    val blogUIDetailState by blogViewModel.blogUIDetailState.collectAsStateWithLifecycle()
    LaunchedEffect(slug) {
        with(Dispatchers.Default) {
            blogViewModel.getBlogBySlug(slug)
        }
    }
    LaunchedEffect(blogUIDetailState) {
        when (blogUIDetailState) {
            is BlogUIState.Loading -> loaderState.show()
            else -> loaderState.hide()
        }
    }
    AnimatedContent(modifier = Modifier.fillMaxSize(), targetState = blogUIDetailState) { state ->
        when (state) {
            is BlogUIState.Success -> {
                val blog by remember { derivedStateOf { state.data } }
                OverlappingZoomHeaderWithParallax(
                    modifier = modifier,
                    header = {
                        UrlImage(
                            modifier = it,
                            url = blog?.image ?: "",
                            contentDescription = blog?.title
                        )
                    },
                    onBackClick = onBack
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(
                                horizontal = DefaultHorizontalPaddingMedium,
                                vertical = DefaultVerticalPaddingLarge
                            ),
                            verticalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingSmall)
                        ) {
                            CategoriesLabelSection(categories = blog?.categories?: emptyList())
                            Text(
                                text = blog?.title ?: "",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            HorizontalDivider(modifier = Modifier.fillMaxWidth())
                            Text(
                                text = blog?.body ?: "",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            else -> Unit
        }
    }
}