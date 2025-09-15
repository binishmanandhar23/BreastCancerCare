package com.breastcancer.breastcancercare.components.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Tags: ImageVector
    get() {
        if (_Tags != null) return _Tags!!
        
        _Tags = ImageVector.Builder(
            name = "Tags",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveToRelative(15f, 5f)
                lineToRelative(6.3f, 6.3f)
                arcToRelative(2.4f, 2.4f, 0f, false, true, 0f, 3.4f)
                lineTo(17f, 19f)
            }
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(9.586f, 5.586f)
                arcTo(2f, 2f, 0f, false, false, 8.172f, 5f)
                horizontalLineTo(3f)
                arcToRelative(1f, 1f, 0f, false, false, -1f, 1f)
                verticalLineToRelative(5.172f)
                arcToRelative(2f, 2f, 0f, false, false, 0.586f, 1.414f)
                lineTo(8.29f, 18.29f)
                arcToRelative(2.426f, 2.426f, 0f, false, false, 3.42f, 0f)
                lineToRelative(3.58f, -3.58f)
                arcToRelative(2.426f, 2.426f, 0f, false, false, 0f, -3.42f)
                close()
            }
            path(
                fill = SolidColor(Color.Black),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(7f, 9.5f)
                arcTo(0.5f, 0.5f, 0f, false, true, 6.5f, 10f)
                arcTo(0.5f, 0.5f, 0f, false, true, 6f, 9.5f)
                arcTo(0.5f, 0.5f, 0f, false, true, 7f, 9.5f)
                close()
            }
        }.build()
        
        return _Tags!!
    }

private var _Tags: ImageVector? = null

