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

fun String.removeSpaces() = replace(" ", "")

val LoremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin at ullamcorper urna. Pellentesque tincidunt arcu eget purus ornare, eget consequat urna mattis. Proin condimentum arcu ut posuere venenatis. Nulla ullamcorper massa pharetra sapien faucibus ullamcorper. Quisque mattis massa enim, eu posuere leo blandit eu. Aenean a sem sit amet elit rutrum lacinia. Donec eget augue hendrerit, laoreet massa sed, iaculis tellus. Proin odio nisl, accumsan vitae dui sit amet, ultrices lobortis tellus. Suspendisse pretium ultricies mi sit amet luctus. Sed vel ipsum a nibh tincidunt dictum id eget lacus.\n" +
        "\n" +
        "Vivamus quis imperdiet leo, ac semper ante. Phasellus nulla diam, accumsan in semper et, blandit sit amet nibh. Mauris iaculis, libero a eleifend malesuada, enim massa rutrum odio, id eleifend diam eros id elit. Quisque ante sapien, commodo ut eros a, ullamcorper condimentum nunc. Sed non mattis lectus. Cras nec egestas diam. Nam vel dolor tellus. Phasellus rhoncus nibh tellus, at accumsan libero tempor ac.\n" +
        "\n" +
        "Sed eu tristique dui. Duis suscipit, magna id hendrerit ultrices, nunc purus rhoncus felis, id consectetur orci mauris sed eros. Phasellus ultricies quis erat sit amet venenatis. Sed id accumsan nulla. Suspendisse tempus arcu sit amet massa sagittis, ac pretium massa varius. Cras ac mi at enim iaculis bibendum et id lacus. Integer tristique condimentum sollicitudin.\n" +
        "\n" +
        "Aenean congue maximus sapien, vel hendrerit urna sodales id. Vestibulum eleifend ex a dui porttitor ultricies. Sed id metus ut magna condimentum pulvinar vitae eget tellus. Mauris ultricies nunc at bibendum dictum. Sed faucibus dictum mi sit amet varius. Integer blandit, purus et sodales accumsan, purus purus molestie nibh, nec elementum urna sem vel ligula. Suspendisse ipsum nisl, ultrices at venenatis eget, luctus vitae ipsum. Donec non lectus pharetra, fringilla nunc nec, vulputate nulla. Nunc in tellus at tortor fringilla fringilla. Nam vitae est sollicitudin orci eleifend venenatis. Proin id condimentum sapien.\n" +
        "\n" +
        "Sed dapibus condimentum odio, viverra lobortis dolor. Etiam hendrerit, neque elementum tempus scelerisque, ligula leo fringilla leo, eu malesuada lorem metus sed libero. Aliquam rutrum sem dui, quis egestas diam commodo at. In hac habitasse platea dictumst. Nulla fermentum lectus gravida aliquam mattis. Vestibulum at mi venenatis, rutrum ex et, tempus arcu. Praesent nibh velit, lobortis a justo quis, volutpat sollicitudin massa. Ut varius semper arcu nec lobortis. Phasellus at tempor nibh, ac mattis libero."