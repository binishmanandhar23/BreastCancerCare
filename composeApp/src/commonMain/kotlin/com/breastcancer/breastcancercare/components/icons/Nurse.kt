// Nurse.kt
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
 * Nurse icon (24 x 24), stroke-based & tintable.
 * Use with: Icon(AppIcons.Nurse, contentDescription = "Nurse")
 */
val Nurse: ImageVector
    get() {
        if (_nurse != null) return _nurse!!
        _nurse = Builder(
            name = "Nurse",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // === Nurse hat (rounded rectangle) ===
            // Rect bounds: left=6.75, top=4.75, right=17.25, bottom=8.25, corner radius ~1.2
            run {
                val l = 6.75f
                val t = 4.75f
                val r = 17.25f
                val b = 8.25f
                val rad = 1.2f
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
                    // top-right corner
                    arcToRelative(
                        rad, rad, 0f,
                        false, true,
                        rad, rad
                    )
                    verticalLineTo(b - rad)
                    // bottom-right corner
                    arcToRelative(
                        rad, rad, 0f,
                        false, true,
                        -rad, rad
                    )
                    horizontalLineTo(l + rad)
                    // bottom-left corner
                    arcToRelative(
                        rad, rad, 0f,
                        false, true,
                        -rad, -rad
                    )
                    verticalLineTo(t + rad)
                    // top-left corner
                    arcToRelative(
                        rad, rad, 0f,
                        false, true,
                        rad, -rad
                    )
                    close()
                }
            }

            // === Cross on hat ===
            path(
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                // vertical stroke of cross
                moveTo(12f, 5.6f)
                lineTo(12f, 7.4f)
                // horizontal stroke of cross
                moveTo(11.1f, 6.5f)
                lineTo(12.9f, 6.5f)
            }

            // === Head (circle via two arcs) ===
            // Center = (12, 11), radius = 3.5 → start on the rightmost point (15.5, 11)
            path(
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(15.5f, 11f)
                // large-arc + sweep flags draw a full circle with two 180° arcs
                arcToRelative(3.5f, 3.5f, 0f, true, true, -7.0f, 0f)
                arcToRelative(3.5f, 3.5f, 0f, true, true, 7.0f, 0f)
                close()
            }

            // === Shoulders / scrubs (semi-ellipse) ===
            // Approximated with a wide arc and a flat base
            path(
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            ) {
                // left base
                moveTo(4f, 20.25f)
                // up slightly to start the arch
                lineTo(4f, 19.75f)
                // draw a wide arch to the right side; rx≈8, ry≈7.25 over dx=16, dy=0
                arcToRelative(8f, 7.25f, 0f, false, true, 16f, 0f)
                // down to base and back to left
                lineTo(20f, 20.25f)
                lineTo(4f, 20.25f)
                close()
            }
        }.build()
        return _nurse!!
    }

private var _nurse: ImageVector? = null
