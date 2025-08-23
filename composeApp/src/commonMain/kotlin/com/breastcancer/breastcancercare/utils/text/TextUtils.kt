package com.breastcancer.breastcancercare.utils.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.AnnotatedString.Builder
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink

class LinkScope(
    private val b: Builder,
    private val onClick: (String) -> Unit
) {
    fun append(text: String) { b.append(text) }

    fun withClickable(tag: String, content: Builder.() -> Unit) {
        b.withLink(LinkAnnotation.Clickable(tag) { onClick(tag) }) {
            b.content()
        }
    }
}

@Composable
fun ClickableText(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    onClick: (String) -> Unit,
    builder: LinkScope.() -> Unit
) {
    val text = buildAnnotatedString {
        LinkScope(this, onClick).builder()
    }
    Text(modifier = modifier, text = text, style = textStyle)
}

object TextUtils {
    val CustomWidgetTextBold =
        TextStyle(fontStyle = FontStyle.Companion.Normal, fontWeight = FontWeight.Companion.Bold)

    val CustomWidgetTextRegular =
        TextStyle(fontStyle = FontStyle.Companion.Normal, fontWeight = FontWeight.Companion.Normal)

    fun String.splitKeeping(str: String): List<String> {
        return this.split(str).flatMap { listOf(it, str) }.dropLast(1).filterNot { it.isEmpty() }
    }

    fun String.splitKeeping(vararg strs: String): List<String> {
        var res = listOf(this)
        strs.forEach { str ->
            res = res.flatMap { it.splitKeeping(str) }
        }
        return res
    }

    fun String.splitKeeping(strings: List<String>): List<String> {
        var res = listOf(this)
        strings.forEach { str ->
            res = res.flatMap { it.splitKeeping(str) }
        }
        return res
    }


    fun String.countMatches(pattern: String): Int {
        return this.split(pattern)
            .dropLastWhile { it.isEmpty() }
            .toTypedArray().size - 1
    }
}