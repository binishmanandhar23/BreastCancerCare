package com.breastcancer.breastcancercare.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.sp
import com.breastcancer.breastcancercare.models.ActivityDTO
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingSmall
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.format.char
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.breastcancer.breastcancercare.database.local.types.Suitability
import com.breastcancer.breastcancercare.models.BlogCategoryDTO
import com.breastcancer.breastcancercare.models.SuitabilityDTO
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultTopHeaderTextSize
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingSmall
import com.breastcancer.breastcancercare.utils.DefaultImage
import com.breastcancer.breastcancercare.utils.DefaultSpacer
import com.breastcancer.breastcancercare.utils.PentagonShape
import com.breastcancer.breastcancercare.utils.TriangleShape


@OptIn(FormatStringsInDatetimeFormats::class)
@Composable
fun ActivityDesign(
    modifier: Modifier,
    selectedDate: LocalDate,
    activityDTO: ActivityDTO,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = DefaultHorizontalPaddingSmall,
                vertical = DefaultVerticalPaddingMedium
            ),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = selectedDate.format(LocalDate.Format { dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED) }),
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = selectedDate.format(LocalDate.Format { byUnicodePattern("dd") }),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                TimeAndDateFormat(activityDTO = activityDTO, selectedDate = selectedDate)
                Text(
                    text = activityDTO.title,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
                activityDTO.location?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Text(
                    text = activityDTO.description,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
fun TimeAndDateFormat(activityDTO: ActivityDTO, selectedDate: LocalDate) = Row {
    Text(
        text = selectedDate.format(LocalDate.Format {
            monthName(MonthNames.ENGLISH_FULL)
            char(' ')
            day()
        }),
        style = MaterialTheme.typography.labelSmall
    )
    activityDTO.startTime?.let {
        Text(
            text = it.format(LocalTime.Format {
                char(' ')
                char('@')
                char(' ')
                amPmHour()
                char(':')
                minute()
                amPmMarker("am", "pm")
            }),
            style = MaterialTheme.typography.labelSmall
        )
    }
    activityDTO.endTime?.let {
        Text(
            text = it.format(LocalTime.Format {
                char(' ')
                char('-')
                char(' ')
                amPmHour()
                char(':')
                minute()
                amPmMarker("am", "pm")
            }),
            style = MaterialTheme.typography.labelSmall
        )
    }
}


@Composable
fun FeaturedLabel(modifier: Modifier = Modifier) = Card(
    modifier = modifier,
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
    shape = MaterialTheme.shapes.extraLarge
) {
    Row(
        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            modifier = Modifier.size(12.dp),
            imageVector = Icons.Default.Bookmark,
            contentDescription = "Featured"
        )
        Text(
            "Featured",
            style = LocalTextStyle.current.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun CoreCustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
    ),
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.W500
    ),
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        maxLines = maxLines,
        label = label,
        colors = colors,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        interactionSource = interactionSource
    )
}

@Composable
fun CoreTextFieldWithBorders(
    modifier: Modifier = Modifier,
    errorText: String? = null,
    errorIcon: ImageVector? = null,
    errorTextColor: Color = MaterialTheme.colorScheme.error,
    errorIconColor: Color = Color.Red,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    shape: Shape = RoundedCornerShape(14.dp),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable BoxScope.() -> Unit
) {
    val isFocused by interactionSource.collectIsFocusedAsState()
    val stroke = if (isFocused) focusedBorderColor else borderColor

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Box(
            modifier = Modifier
                .border(width = 1.5.dp, color = stroke, shape = shape)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            content = content
        )
        ErrorRow(
            errorText = errorText,
            errorIcon = errorIcon,
            errorTextColor = errorTextColor,
            errorIconColor = errorIconColor
        )
    }
}


@Composable
fun TextFieldOuterBox(
    modifier: Modifier = Modifier,
    errorText: String? = null,
    errorIcon: ImageVector? = null,
    errorTextColor: Color = MaterialTheme.colorScheme.error,
    errorIconColor: Color = Color.Red,
    content: @Composable BoxScope.() -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Box(
            modifier = Modifier.border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = MaterialTheme.shapes.small
            ),
            content = content
        )
        ErrorRow(
            errorText = errorText,
            errorIcon = errorIcon,
            errorTextColor = errorTextColor,
            errorIconColor = errorIconColor
        )
    }

}

@Composable
fun ErrorRow(
    errorText: String?,
    errorIcon: ImageVector?,
    errorTextColor: Color = MaterialTheme.colorScheme.error,
    errorIconColor: Color = Color.Red
) {
    AnimatedVisibility(visible = !errorText.isNullOrEmpty()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (errorIcon != null)
                Icon(
                    imageVector = errorIcon,
                    contentDescription = errorText,
                    tint = errorIconColor
                )
            if (!errorText.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    text = errorText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = errorTextColor,
                        fontWeight = FontWeight.Light
                    )
                )
            }
        }
    }
}

@Composable
fun BreastCancerSingleLineTextField(
    modifier: Modifier = Modifier,
    value: String,
    errorText: String? = null,
    errorIcon: ImageVector? = null,
    errorTextColor: Color = MaterialTheme.colorScheme.error,
    errorIconColor: Color = Color.Red,
    onValueChange: (String) -> Unit,
    label: String,
    labelIcon: ImageVector? = null,
    enabled: Boolean = true,
    labelIconRes: DrawableResource? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
    ),
    borderColor: Color = MaterialTheme.colorScheme.outline,
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    shape: Shape = RoundedCornerShape(14.dp)
) {
    val interaction = remember { MutableInteractionSource() }

    CoreTextFieldWithBorders(
        modifier = modifier,
        errorText = errorText,
        errorIcon = errorIcon,
        errorTextColor = errorTextColor,
        errorIconColor = errorIconColor,
        borderColor = borderColor,
        focusedBorderColor = focusedBorderColor,
        shape = shape,
        interactionSource = interaction
    ) {
        CoreCustomTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            label = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (labelIcon != null) Icon(
                        imageVector = labelIcon,
                        contentDescription = label
                    )
                    if (labelIconRes != null) Icon(
                        painter = painterResource(labelIconRes),
                        contentDescription = label
                    )
                    Text(text = label)
                }
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            maxLines = 1,
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            leadingIcon = leadingIcon,
            colors = colors,
            interactionSource = interaction
        )
    }
}


@Composable
fun BreastCancerMultiLineTextField(
    modifier: Modifier = Modifier,
    value: String,
    errorText: String? = null,
    errorIcon: ImageVector? = null,
    errorTextColor: Color = MaterialTheme.colorScheme.error,
    errorIconColor: Color = Color.Red,
    onValueChange: (String) -> Unit,
    label: String,
    labelIcon: ImageVector? = null,
    enabled: Boolean = true,
    labelIconRes: DrawableResource? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    maxLines: Int = 3,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    CoreTextFieldWithBorders(
        modifier = modifier,
        errorText = errorText,
        errorIcon = errorIcon,
        errorTextColor = errorTextColor,
        errorIconColor = errorIconColor
    ) {
        CoreCustomTextField(
            modifier = modifier,
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            label = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (labelIcon != null)
                        Icon(imageVector = labelIcon, contentDescription = label)
                    if (labelIconRes != null)
                        Icon(
                            painter = painterResource(labelIconRes),
                            contentDescription = label
                        )
                    Text(text = label)
                }
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            leadingIcon = leadingIcon
        )
    }
}

@Composable
fun BreastCancerButton(
    modifier: Modifier = Modifier,
    text: String,
    allCaps: Boolean = false,
    shape: CornerBasedShape = MaterialTheme.shapes.medium,
    fontSize: TextUnit = MaterialTheme.typography.bodySmall.fontSize,
    contentPadding: PaddingValues = PaddingValues(horizontal = 22.dp, vertical = 12.dp),
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    hideText: Boolean = false,
    onDisabledClick: (() -> Unit)? = null,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(if (enabled) backgroundColor else MaterialTheme.colorScheme.outline)
    Button(
        modifier = modifier.animateContentSize(),
        onClick = { if (enabled) onClick() else onDisabledClick?.invoke() },
        contentPadding = contentPadding,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = shape
    ) {
        leadingIcon?.let {
            Icon(imageVector = leadingIcon, contentDescription = text)
            if (!hideText)
                Spacer(modifier = Modifier.size(10.dp))
        }
        AnimatedVisibility(!hideText) {
            Text(
                text = text.let {
                    if (allCaps)
                        it.uppercase()
                    else
                        it
                },
                style = MaterialTheme.typography.bodySmall.copy(
                    color = textColor,
                    fontSize = fontSize
                )
            )
        }
    }
}


@Composable
fun AutoSizeText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyLarge
) {
    var resizableTextSize by remember(style) {
        mutableStateOf(style)
    }
    val defaultFontSize = remember {
        16.sp
    }
    var shouldDraw by remember {
        mutableStateOf(false)
    }

    Text(modifier = modifier.drawWithContent {
        if (shouldDraw)
            drawContent()
    }, text = text, style = resizableTextSize, onTextLayout = {
        if (it.didOverflowWidth) {
            resizableTextSize =
                resizableTextSize.copy(fontSize = (if (resizableTextSize.fontSize.isUnspecified) defaultFontSize else resizableTextSize.fontSize) * 0.95f)
        } else
            shouldDraw = true
    }, softWrap = false)
}

@Composable
fun BreastCancerAlertDialog(
    title: String,
    text: @Composable (() -> Unit)? = null,
    confirmText: String = "Ok",
    dismissText: String? = "Cancel",
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) = AlertDialog(
    onDismissRequest = onDismissRequest,
    title = {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
    },
    text = text,
    confirmButton = {
        BreastCancerButton(text = confirmText, onClick = onConfirm)
    },
    dismissButton = {
        if (dismissText != null)
            BreastCancerButton(
                text = dismissText,
                onClick = onDismissRequest,
                backgroundColor = MaterialTheme.colorScheme.background,
                textColor = MaterialTheme.colorScheme.onBackground
            )
    }
)

@Composable
fun BreastCancerToolbar(
    modifier: Modifier = Modifier.fillMaxWidth().padding(
        horizontal = DefaultHorizontalPaddingSmall,
        vertical = DefaultVerticalPaddingMedium
    ),
    title: String,
    onBack: () -> Unit
) = Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    Icon(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onBack
        ), imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Back Button"
    )
    Text(
        title,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
    )
}

@Composable
fun SuitabilityFilterChips(
    modifier: Modifier = Modifier.fillMaxWidth(),
    suitabilities: List<SuitabilityDTO>,
    selected: SuitabilityDTO? = null,
    onClick: (SuitabilityDTO?) -> Unit
) {
    val colors = FilterChipDefaults.filterChipColors(
        containerColor = Color.Transparent,
        selectedContainerColor = MaterialTheme.colorScheme.primary,
        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
    )

    LazyRow(
        modifier = modifier.background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.background,
                    MaterialTheme.colorScheme.background.copy(0.8f),
                )
            )
        )
            .padding(
                horizontal = DefaultHorizontalPaddingSmall,
                vertical = DefaultVerticalPaddingSmall
            ),
        horizontalArrangement = Arrangement.spacedBy(DefaultHorizontalPaddingSmall)
    ) {
        item {
            FilterChip(
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "All",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },
                selected = selected == null,
                onClick = {
                    onClick(null)
                },
                colors = colors
            )
        }
        items(suitabilities) { suitability ->
            FilterChip(label = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    SuitabilityShape(suitability = suitability)
                    Text(
                        text = suitability.name,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }, selected = selected == suitability, onClick = {
                onClick(suitability)
            }, colors = colors)
        }
    }
}

@Composable
fun SuitabilityShape(suitability: SuitabilityDTO) {
    val shape by derivedStateOf {
        when (suitability.key) {
            Suitability.Early.key -> TriangleShape
            Suitability.Newly.key -> CircleShape
            Suitability.Metastatic.key -> PentagonShape
            else -> CircleShape
        }
    }
    Box(
        modifier = Modifier.size(15.dp).let {
            when (suitability.key) {
                Suitability.Early.key ->
                    it.background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = shape
                    )

                Suitability.Newly.key ->
                    it.background(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = shape
                    )

                Suitability.Metastatic.key ->
                    it.background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = shape
                    )

                else -> it
            }.border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.background,
                shape = shape
            )
        }
    )
}

@Composable
fun BreastCancerCircularLoader(modifier: Modifier = Modifier.size(40.dp)) =
    Box(modifier = modifier) {
        CircularProgressIndicator(
            modifier = modifier,
            color = MaterialTheme.colorScheme.primary
        )
    }

@Composable
fun UrlImage(url: String, contentDescription: String? = null, modifier: Modifier = Modifier) {
    var painterState by remember { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }
    SubcomposeAsyncImage(
        model = url,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        onState = {
            painterState = it
        }
    ) {
        when (painterState) {
            is AsyncImagePainter.State.Loading ->
                BreastCancerCircularLoader()

            is AsyncImagePainter.State.Error ->
                DefaultImage()

            else -> SubcomposeAsyncImageContent()
        }
    }
}

@Composable
fun CoreHomeCardDesign(
    modifier: Modifier,
    image: @Composable ColumnScope.() -> Unit = {
        DefaultImage(
            modifier = Modifier.fillMaxWidth().height(150.dp), contentScale = ContentScale.Crop
        )
    },
    title: @Composable ColumnScope.() -> Unit,
    subtitle: @Composable ColumnScope.() -> Unit,
    categories: @Composable (ColumnScope.() -> Unit)? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = MaterialTheme.shapes.large,
        onClick = onClick
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            image()
            Column(
                modifier = Modifier.padding(
                    horizontal = DefaultHorizontalPaddingSmall,
                    vertical = DefaultVerticalPaddingSmall
                )
            ) {
                categories?.invoke(this)
                title()
                subtitle()
            }
        }
    }
}

@Composable
fun CategoriesLabelSection(categories: List<BlogCategoryDTO>) {
    FlowRow(
        maxItemsInEachRow = 3,
        modifier = Modifier.fillMaxWidth().padding(vertical = DefaultVerticalPaddingSmall),
        horizontalArrangement = Arrangement.spacedBy(DefaultHorizontalPaddingSmall),
    ) {
        categories.forEach { category ->
            CategoryChip(categoryName = category.name)
        }
    }
}

@Composable
fun CategoryChip(
    modifier: Modifier = Modifier, categoryName: String,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary
    ),
    border: BorderStroke? = null,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
    onClick: (() -> Unit)? = null,
) = Card(
    modifier = modifier,
    shape = MaterialTheme.shapes.extraSmall,
    colors = colors,
    border = border,
    onClick = { onClick?.invoke() }
) {
    Text(
        modifier = Modifier.padding(vertical = 3.dp, horizontal = 5.dp),
        text = categoryName,
        style = textStyle
    )
}

@Composable
fun BreastCancerBackButton(
    modifier: Modifier = Modifier.padding(
        horizontal = DefaultHorizontalPaddingMedium,
        vertical = DefaultVerticalPaddingMedium
    ), onBackClick: () -> Unit
) {
    Box(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.background.copy(
                alpha = 0.5f
            ), shape = CircleShape
        ).clip(CircleShape).clickable {
            onBackClick()
        }.padding(5.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = "Back button"
        )
    }
}

@Composable
fun <T> AllListContainer(
    title: String,
    listOfCategories: List<T>,
    selectedCategory: T?,
    categorySectionContent: LazyListScope.(borderStroke: BorderStroke) -> Unit,
    onBack: () -> Unit,
    onAllClicked: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumnCollapsibleHeader(
            modifier = Modifier.fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            collapseWithFade = true,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingMedium),
            header = {
                Text(
                    modifier = Modifier.align(Alignment.TopStart)
                        .padding(start = DefaultHorizontalPaddingLarge + DefaultHorizontalPaddingMedium),
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = DefaultTopHeaderTextSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }) {
            stickyHeader {
                val borderStroke =
                    BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
                AnimatedVisibility(listOfCategories.isNotEmpty()) {
                    Column {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                                .background(color = MaterialTheme.colorScheme.background)
                        )
                        LazyRow(
                            modifier = Modifier.fillMaxWidth().background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.background,
                                        MaterialTheme.colorScheme.background.copy(0.8f),
                                    )
                                )
                            ).padding(vertical = DefaultVerticalPaddingMedium),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(
                                DefaultHorizontalPaddingSmall
                            )
                        ) {
                            item { DefaultSpacer() }
                            item {
                                val selectedContainerColor by animateColorAsState(if (selectedCategory == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
                                val selectedContentColor by animateColorAsState(if (selectedCategory == null) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground)
                                CategoryChip(
                                    categoryName = "All",
                                    border = borderStroke,
                                    colors = CardDefaults.cardColors(
                                        containerColor = selectedContainerColor,
                                        contentColor = selectedContentColor
                                    ),
                                    onClick = onAllClicked
                                )
                            }
                            categorySectionContent(borderStroke)
                            item { DefaultSpacer() }
                        }
                    }
                }
            }
            content()
            item {
                DefaultSpacerSize()
            }
        }
        BreastCancerBackButton(onBackClick = onBack)
    }
}

