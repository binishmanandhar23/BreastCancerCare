// Counselling.kt
package com.breastcancer.breastcancercare.components.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Counselling icon (24 x 24), stroke-based bubbles + filled dots.
 * Usage: Icon(AppIcons.Counselling, contentDescription = "Counselling")
 */
val Counselling: ImageVector
    get() {
        if (_counselling != null) return _counselling!!
        _counselling = Builder(
            name = "Counselling",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // === Back chat bubble (top-left) ===
            run {
                val l = 3.5f
                val t = 4f
                val r = 15.5f
                val b = 11f
                val rad = 2f
                // Rounded rectangle outline
                path(
                    fill = SolidColor(Color(0x00000000)),
                    stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 1.5f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(l + rad, t)
                    horizontalLineTo(r - rad)
                    arcToRelative(rad, rad, 0f, false, true, rad, rad) // top-right
                    verticalLineTo(b - rad)
                    arcToRelative(rad, rad, 0f, false, true, -rad, rad) // bottom-right
                    horizontalLineTo(l + rad)
                    arcToRelative(rad, rad, 0f, false, true, -rad, -rad) // bottom-left
                    verticalLineTo(t + rad)
                    arcToRelative(rad, rad, 0f, false, true, rad, -rad) // top-left
                    close()
                }
                // Tail
                path(
                    fill = SolidColor(Color(0x00000000)),
                    stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 1.5f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Round,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(7.6f, b)           // bottom edge
                    lineTo(7.6f, b + 2.5f)    // down
                    lineTo(9.5f, b - 0.5f)    // back towards bubble
                }
            }

            // === Front chat bubble (bottom-right) ===
            run {
                val l = 8.5f
                val t = 9.5f
                val r = 20.5f
                val b = 17f
                val rad = 2f
                // Rounded rectangle outline
                path(
                    fill = SolidColor(Color(0x00000000)),
                    stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 1.5f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(l + rad, t)
                    horizontalLineTo(r - rad)
                    arcToRelative(rad, rad, 0f, false, true, rad, rad) // top-right
                    verticalLineTo(b - rad)
                    arcToRelative(rad, rad, 0f, false, true, -rad, rad) // bottom-right
                    horizontalLineTo(l + rad)
                    arcToRelative(rad, rad, 0f, false, true, -rad, -rad) // bottom-left
                    verticalLineTo(t + rad)
                    arcToRelative(rad, rad, 0f, false, true, rad, -rad) // top-left
                    close()
                }
                // Tail (bottom-right)
                path(
                    fill = SolidColor(Color(0x00000000)),
                    stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 1.5f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Round,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(16.4f, b)
                    lineTo(17.8f, b + 2.6f)
                    lineTo(18.2f, b - 0.5f)
                }
            }

            // === Dots inside the front bubble (filled) ===
            fun dot(cx: Float, cy: Float, r: Float) {
                path(
                    fill = SolidColor(Color(0xFF000000)),
                    stroke = null,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(cx + r, cy)
                    // draw full circle using two 180° arcs
                    arcToRelative(r, r, 0f, true, true, -2 * r, 0f)
                    arcToRelative(r, r, 0f, true, true, 2 * r, 0f)
                    close()
                }
            }
            dot(cx = 11.0f, cy = 13.0f, r = 0.7f)
            dot(cx = 13.0f, cy = 13.0f, r = 0.7f)
            dot(cx = 15.0f, cy = 13.0f, r = 0.7f)
        }.build()
        return _counselling!!
    }

private var _counselling: ImageVector? = null
