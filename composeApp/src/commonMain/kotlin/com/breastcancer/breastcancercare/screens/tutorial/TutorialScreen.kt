package com.breastcancer.breastcancercare.screens.tutorial

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.components.BreastCancerButton
import com.breastcancer.breastcancercare.components.BreastCancerCircularLoader
import com.breastcancer.breastcancercare.components.DefaultSpacerSize
import com.breastcancer.breastcancercare.models.TutorialDTO
import com.breastcancer.breastcancercare.states.TutorialUIState
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.viewmodel.TutorialViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TutorialScreen(
    userId: Long,
    hideSkipButton: Boolean,
    tutorialViewModel: TutorialViewModel = koinViewModel(),
    onFinish: () -> Unit
) {
    val tutorialUIState by tutorialViewModel.tutorialUIState.collectAsStateWithLifecycle()
    Box(modifier = Modifier.fillMaxSize()) {
        when (tutorialUIState) {
            is TutorialUIState.Loading -> BreastCancerCircularLoader(
                modifier = Modifier.align(
                    Alignment.Center
                )
            )

            is TutorialUIState.Success -> {
                val tutorials =
                    remember(tutorialUIState.data) { tutorialUIState.data ?: emptyList() }
                val pagerState = rememberPagerState(pageCount = {
                    tutorials.count()
                })
                val lastPage by remember(pagerState.currentPage) {
                    derivedStateOf {
                        pagerState.currentPage == (pagerState.pageCount - 1)
                    }
                }
                val scope = rememberCoroutineScope()
                val onDone: () -> Unit = {
                    scope.launch {
                        tutorialViewModel.updateTutorialViewedById(
                            userId = userId,
                            tutorialViewed = true
                        )
                        onFinish()
                    }
                }

                TutorialPager(tutorials = tutorials, pagerState = pagerState)
                if (!hideSkipButton)
                    TextButton(
                        modifier = Modifier.align(Alignment.TopEnd)
                            .padding(horizontal = DefaultHorizontalPaddingMedium),
                        onClick = onDone,
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.3f),
                            contentColor = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        Text(
                            text = "Skip",
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            textDecoration = TextDecoration.Underline
                        )
                    }
                Row(
                    modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background.copy(0.5f),
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.colorScheme.background,
                            )
                        )
                    )
                        .padding(
                            vertical = DefaultVerticalPaddingMedium,
                            horizontal = DefaultHorizontalPaddingMedium
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BreastCancerButton(
                        text = "Back",
                        enabled = pagerState.currentPage > 0,
                        onClick = {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                        })
                    Indicator(
                        pagerState = pagerState
                    )
                    BreastCancerButton(text = if (!lastPage) "Next" else "Done", onClick = {
                        if (!lastPage)
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        else
                            onDone()
                    })
                }
            }

            else -> Unit
        }

    }
}

@Composable
private fun TutorialPager(tutorials: List<TutorialDTO>, pagerState: PagerState) {
    HorizontalPager(modifier = Modifier.fillMaxSize(), state = pagerState) { page ->
        TutorialBody(tutorialDTO = tutorials[page])
    }
}

@Composable
private fun TutorialBody(tutorialDTO: TutorialDTO) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState)
            .padding(
                horizontal = DefaultHorizontalPaddingMedium,
                vertical = DefaultVerticalPaddingLarge
            ),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = tutorialDTO.title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Image(
            modifier = Modifier.padding(
                horizontal = DefaultHorizontalPaddingLarge,
                vertical = DefaultVerticalPaddingLarge
            ),
            painter = painterResource(tutorialDTO.image),
            contentDescription = tutorialDTO.title
        )
        Text(
            text = tutorialDTO.description,
            style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center)
        )
        DefaultSpacerSize()
    }
}

@Composable
private fun Indicator(modifier: Modifier = Modifier, pagerState: PagerState) {
    Row(
        modifier
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color by animateColorAsState(
                if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.5f
                )
            )
            val scale by animateFloatAsState(if (pagerState.currentPage == iteration) 1.2f else 1.0f)
            Box(
                modifier = Modifier.scale(scale)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(16.dp)
            )
        }
    }
}