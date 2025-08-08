package com.breastcancer.breastcancercare.components.loader


import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.components.loader.LoaderState.Companion.Saver
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun CustomLoader(
    modifier: Modifier = Modifier,
    loaderState: LoaderState,
    content: @Composable () -> Unit
) {
    val visible by loaderState.visible.collectAsStateWithLifecycle(false)

    Box(modifier = modifier) {
        MainContents(
            content = content,
            visible = visible,
        )
    }

}

@Composable
private fun MainContents(
    modifier: Modifier = Modifier,
    visible: Boolean,
    content: @Composable () -> Unit
) {
    content()
    AnimatedContent(
        modifier = modifier.fillMaxWidth(),
        targetState = visible,
        transitionSpec = {
            fadeIn() togetherWith  fadeOut()
        },
        label = "",
    ) { show ->
        if (show)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.4f))
            ) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.secondary,
                    strokeCap = StrokeCap.Butt
                )
            }
    }
}

@Composable
fun rememberLoaderState(initialVisibility: Boolean = false): LoaderState =
    rememberSaveable(saver = Saver) {
        LoaderState(initialVisibility)
    }


class LoaderState(initialVisibility: Boolean) {
    val visible = MutableStateFlow(initialVisibility)

    fun show() {
        visible.value = true
    }

    fun hide() {
        visible.value = false
    }

    companion object {
        /**
         * The default [Saver] implementation for [LoaderState].
         */
        val Saver: Saver<LoaderState, *> = Saver(
            save = { it.visible.value },
            restore = { LoaderState(it) }
        )
    }
}