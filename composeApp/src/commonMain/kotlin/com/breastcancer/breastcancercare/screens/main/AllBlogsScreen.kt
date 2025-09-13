package com.breastcancer.breastcancercare.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.components.AllListContainer
import com.breastcancer.breastcancercare.components.BreastCancerBackButton
import com.breastcancer.breastcancercare.components.BreastCancerCircularLoader
import com.breastcancer.breastcancercare.components.CategoriesLabelSection
import com.breastcancer.breastcancercare.components.CategoryChip
import com.breastcancer.breastcancercare.components.CoreHomeCardDesign
import com.breastcancer.breastcancercare.components.DefaultSpacerSize
import com.breastcancer.breastcancercare.components.LazyColumnCollapsibleHeader
import com.breastcancer.breastcancercare.components.UrlImage
import com.breastcancer.breastcancercare.components.loader.LoaderState
import com.breastcancer.breastcancercare.models.BlogDTO
import com.breastcancer.breastcancercare.screens.Route
import com.breastcancer.breastcancercare.states.BlogUIState
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingSmall
import com.breastcancer.breastcancercare.theme.DefaultTopHeaderTextSize
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.utils.DefaultSpacer
import com.breastcancer.breastcancercare.viewmodel.BlogViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AllBlogsScreen(
    blogViewModel: BlogViewModel,
    loaderState: LoaderState,
    onBackPress: () -> Unit,
    onSubScreenChange: (Route) -> Unit
) {
    val allCategories by blogViewModel.allCategories.collectAsStateWithLifecycle()
    val blogUILIstState by blogViewModel.blogUIListState.collectAsStateWithLifecycle()
    val selectedCategory by blogViewModel.selectedCategory.collectAsStateWithLifecycle()
    AllListContainer(
        title = "Blogs",
        listOfCategories = allCategories,
        selectedCategory = selectedCategory,
        categorySectionContent = { borderStroke ->
            items(items = allCategories) { category ->
                val selectedContainerColor by animateColorAsState(if (selectedCategory.data?.name == category.name) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
                val selectedContentColor by animateColorAsState(if (selectedCategory.data?.name == category.name) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground)
                CategoryChip(
                    categoryName = category.name,
                    border = borderStroke,
                    colors = CardDefaults.cardColors(
                        containerColor = selectedContainerColor,
                        contentColor = selectedContentColor
                    ), onClick = { blogViewModel.selectCategory(category) }
                )
            }
        },
        onBack = onBackPress,
        onAllClicked = { blogViewModel.selectCategory(null) },
        content = {
            when (blogUILIstState) {
                is BlogUIState.Loading -> item { BreastCancerCircularLoader() }
                is BlogUIState.Success -> items(
                    items = blogUILIstState.data ?: emptyList(),
                    key = { blog -> blog.slug }
                ) { blog ->
                    BlogCard(
                        modifier = Modifier.height(350.dp).fillMaxWidth().animateItem()
                            .padding(horizontal = DefaultHorizontalPaddingMedium),
                        blog = blog,
                        onClick = {
                            onSubScreenChange(Route.Main.BlogDetail(blog.slug))
                        })
                }

                else -> Unit
            }
        }
    )
}



@Composable
private fun BlogCard(
    modifier: Modifier = Modifier,
    blog: BlogDTO,
    onClick: (blog: BlogDTO) -> Unit
) =
    CoreHomeCardDesign(
        onClick = { onClick(blog) },
        modifier = modifier, image = {
            UrlImage(
                modifier = Modifier.fillMaxWidth().height(200.dp),
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