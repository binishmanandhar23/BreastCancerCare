package com.breastcancer.breastcancercare.components.snackbar


import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.components.snackbar.SnackBarState.Companion.Saver
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun CustomSnackBar(
    modifier: Modifier = Modifier,
    snackBarModifier: Modifier = Modifier,
    text: String,
    snackBarState: SnackBarState,
    delay: Long? = null,
    useBox: Boolean = false,
    content: @Composable () -> Unit
) {
    val visible by snackBarState.visible.collectAsStateWithLifecycle(false)
    val overridingText by snackBarState.overridingText.collectAsStateWithLifecycle(text)
    val overridingDelay by snackBarState.overridingDelay.collectAsStateWithLifecycle(delay)
    val innerText by remember (overridingText) { mutableStateOf(overridingText) }

    overridingDelay?.let {
        LaunchedEffect(key1 = visible) {
            delay(it)
            snackBarState.hide()
        }
    }

    if (useBox)
        Box(modifier = modifier) {
            MainContents(
                content = content,
                visible = visible,
                innerText = innerText,
                modifier = snackBarModifier.align(
                    Alignment.BottomCenter
                )
            )
        }
    else
        Column(modifier = modifier) {
            MainContents(
                content = content,
                visible = visible,
                innerText = innerText,
                modifier = snackBarModifier.padding(top = 10.dp)
            )
        }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun MainContents(
    modifier: Modifier,
    visible: Boolean,
    innerText: String,
    content: @Composable () -> Unit
) {
    content()
    AnimatedContent(
        modifier = modifier.fillMaxWidth(),
        targetState = visible,
        transitionSpec = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up) + fadeIn() with slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Down
            ) + fadeOut()
        },
        label = "",
    ) { show ->
        if (show)
            Snackbar(
                modifier = modifier,
                containerColor = MaterialTheme.colorScheme.onBackground,
                shape = MaterialTheme.shapes.medium,
            ) {
                Text(
                    text = innerText
                )
            }
        else
            Spacer(modifier = modifier.fillMaxWidth())
    }
}

@Composable
fun rememberSnackBarState(initialVisibility: Boolean = false): SnackBarState =
    rememberSaveable(saver = SnackBarState.Saver) {
        SnackBarState(initialVisibility)
    }

const val SnackBarLengthShort = 1500L
const val SnackBarLengthMedium = 2200L
const val SnackBarLengthLong = 3000L

class SnackBarState(initialVisibility: Boolean) {
    val visible = MutableStateFlow(initialVisibility)
    val overridingText = MutableStateFlow("")
    val overridingDelay = MutableStateFlow<Long?>(null)

    fun show(overridingText: String? = null, overridingDelay: Long? = null) {
        visible.value = true
        if (overridingText != null)
            this.overridingText.value = overridingText

        this.overridingDelay.value = overridingDelay
    }

    fun hide() {
        visible.value = false
    }

    companion object {
        /**
         * The default [Saver] implementation for [SnackBarState].
         */
        val Saver: Saver<SnackBarState, *> = Saver(
            save = { it.visible.value },
            restore = { SnackBarState(it) }
        )
    }
}