package com.breastcancer.breastcancercare.utils.text

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.AnnotatedString.Builder
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import kotlin.math.roundToInt

class LinkScope(
    private val b: Builder,
    private val onClick: (String) -> Unit
) {
    fun append(text: String) {
        b.append(text)
    }

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

@Composable
fun TextWithHeight(modifier: Modifier, text: AnnotatedString, fontSize: TextUnit, onHeightChanged: (height: Dp) -> Unit) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    BoxWithConstraints(modifier = modifier) {
        val style = TextStyle(fontSize = fontSize, fontWeight = FontWeight.Bold)
        val textLayoutResult: TextLayoutResult = textMeasurer.measure(
            text = text,
            style = style,
            constraints = Constraints(maxWidth = constraints.maxWidth) // Pass the available width
        )
        val textHeight = with(density) { (textLayoutResult.size.height * 1.3f).toDp() }

        LaunchedEffect(textHeight){
           onHeightChanged(textHeight)
        }

        Text(
            text = text,
            style = style,
            modifier = Modifier.height(textHeight) // Apply the measured height if needed
        )
    }
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

@Composable
fun LinkText(
    modifier: Modifier = Modifier,
    url: String,
    color: Color = MaterialTheme.colorScheme.onBackground,
    style: TextStyle = MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.Underline)
) {
    val uri = LocalUriHandler.current
    Text(
        modifier = modifier.clickable { uri.openUri(url) },
        text = url,
        color = color,
        style = style,
    )
}

fun String.removeSpaces() = replace(" ", "")

val LoremIpsum =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin at ullamcorper urna. Pellentesque tincidunt arcu eget purus ornare, eget consequat urna mattis. Proin condimentum arcu ut posuere venenatis. Nulla ullamcorper massa pharetra sapien faucibus ullamcorper. Quisque mattis massa enim, eu posuere leo blandit eu. Aenean a sem sit amet elit rutrum lacinia. Donec eget augue hendrerit, laoreet massa sed, iaculis tellus. Proin odio nisl, accumsan vitae dui sit amet, ultrices lobortis tellus. Suspendisse pretium ultricies mi sit amet luctus. Sed vel ipsum a nibh tincidunt dictum id eget lacus.\n" +
            "\n" +
            "Vivamus quis imperdiet leo, ac semper ante. Phasellus nulla diam, accumsan in semper et, blandit sit amet nibh. Mauris iaculis, libero a eleifend malesuada, enim massa rutrum odio, id eleifend diam eros id elit. Quisque ante sapien, commodo ut eros a, ullamcorper condimentum nunc. Sed non mattis lectus. Cras nec egestas diam. Nam vel dolor tellus. Phasellus rhoncus nibh tellus, at accumsan libero tempor ac.\n" +
            "\n" +
            "Sed eu tristique dui. Duis suscipit, magna id hendrerit ultrices, nunc purus rhoncus felis, id consectetur orci mauris sed eros. Phasellus ultricies quis erat sit amet venenatis. Sed id accumsan nulla. Suspendisse tempus arcu sit amet massa sagittis, ac pretium massa varius. Cras ac mi at enim iaculis bibendum et id lacus. Integer tristique condimentum sollicitudin.\n" +
            "\n" +
            "Aenean congue maximus sapien, vel hendrerit urna sodales id. Vestibulum eleifend ex a dui porttitor ultricies. Sed id metus ut magna condimentum pulvinar vitae eget tellus. Mauris ultricies nunc at bibendum dictum. Sed faucibus dictum mi sit amet varius. Integer blandit, purus et sodales accumsan, purus purus molestie nibh, nec elementum urna sem vel ligula. Suspendisse ipsum nisl, ultrices at venenatis eget, luctus vitae ipsum. Donec non lectus pharetra, fringilla nunc nec, vulputate nulla. Nunc in tellus at tortor fringilla fringilla. Nam vitae est sollicitudin orci eleifend venenatis. Proin id condimentum sapien.\n" +
            "\n" +
            "Sed dapibus condimentum odio, viverra lobortis dolor. Etiam hendrerit, neque elementum tempus scelerisque, ligula leo fringilla leo, eu malesuada lorem metus sed libero. Aliquam rutrum sem dui, quis egestas diam commodo at. In hac habitasse platea dictumst. Nulla fermentum lectus gravida aliquam mattis. Vestibulum at mi venenatis, rutrum ex et, tempus arcu. Praesent nibh velit, lobortis a justo quis, volutpat sollicitudin massa. Ut varius semper arcu nec lobortis. Phasellus at tempor nibh, ac mattis libero."

val StartingStrongDescription = "Starting Strong\n" +
        "\n" +
        "Starting Strong is a supportive program designed for people who have recently been diagnosed with breast cancer. The program brings together a team of nurses and counsellors to provide holistic care at a time when it’s most needed.\n" +
        "\n" +
        "Through nursing support, participants receive guidance on treatment, symptom management, and ongoing health needs. Our counsellors offer emotional support to help navigate the challenges that come with a diagnosis.\n" +
        "\n" +
        "You may also be eligible for our financial and practical support service which helps with managing costs, accessing resources, and reducing day-to-day stressors.\n" +
        "\n" +
        "Starting Strong is about more than just treatment — it’s about giving people the confidence, knowledge, and support to move forward with strength and dignity."

val LivingWellDescription = "Living Well\n" +
        "\n" +
        "The Living Well Program offers a comprehensive suite of wellness activities, interactive workshops, educational webinars, discussion groups, and community events. These programs are designed to empower you if you have completed active treatment for early breast cancer or if you are living well with metastatic breast cancer. \n" +
        "\n" +
        "Some activities or workshops will be available to both those who have early breast cancer and those who have metastatic breast cancer, while others are tailored specifically for one group. \n" +
        "\n" +
        "The Living Well program aims to optimise health and wellbeing for Breast Cancer Care WA clients. To maximise safety and outcomes, we require annual medical clearance for clients who are living with metastatic breast cancer. We also require updated medical clearance from your treating specialist if there is disease progression / a change to medical treatment.   \n" +
        "\n" +
        "Program Highlights: \n" +
        "\n" +
        "- Wellness Activities: Engage in mind-body activities aimed at enhancing physical and mental well-being. Please note, we are currently developing this part of our Living Well program and some activities will as such be limited at this time.\n" +
        "- Interactive Workshops: Participate in sessions that provide practical skills and knowledge to support your post-treatment journey or when living well after a metastatic breast cancer diagnosis \n" +
        "- Educational Webinars: Access expert-led discussions on topics relevant to life with and beyond breast cancer treatment. \n" +
        "- Discussion Groups: Connect with peers to discuss specific topics and offer mutual support. \n" +
        "- Community Events: Join events that foster a sense of community and shared purpose. "