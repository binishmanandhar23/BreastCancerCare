package com.breastcancer.breastcancercare.components.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Switch_account: ImageVector
    get() {
        if (_Switch_account != null) return _Switch_account!!
        
        _Switch_account = ImageVector.Builder(
            name = "Switch_account",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000))
            ) {
                moveTo(560f, 440f)
                quadToRelative(50f, 0f, 85f, -35f)
                reflectiveQuadToRelative(35f, -85f)
                reflectiveQuadToRelative(-35f, -85f)
                reflectiveQuadToRelative(-85f, -35f)
                reflectiveQuadToRelative(-85f, 35f)
                reflectiveQuadToRelative(-35f, 85f)
                reflectiveQuadToRelative(35f, 85f)
                reflectiveQuadToRelative(85f, 35f)
                moveTo(320f, 630f)
                quadToRelative(45f, -53f, 108f, -81.5f)
                reflectiveQuadTo(560f, 520f)
                reflectiveQuadToRelative(132f, 28.5f)
                reflectiveQuadTo(800f, 630f)
                verticalLineToRelative(-470f)
                horizontalLineTo(320f)
                close()
                moveToRelative(0f, 90f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(240f, 640f)
                verticalLineToRelative(-480f)
                quadToRelative(0f, -33f, 23.5f, -56.5f)
                reflectiveQuadTo(320f, 80f)
                horizontalLineToRelative(480f)
                quadToRelative(33f, 0f, 56.5f, 23.5f)
                reflectiveQuadTo(880f, 160f)
                verticalLineToRelative(480f)
                quadToRelative(0f, 33f, -23.5f, 56.5f)
                reflectiveQuadTo(800f, 720f)
                close()
                moveTo(160f, 880f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(80f, 800f)
                verticalLineToRelative(-560f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(560f)
                horizontalLineToRelative(560f)
                verticalLineToRelative(80f)
                close()
                moveToRelative(400f, -520f)
                quadToRelative(-17f, 0f, -28.5f, -11.5f)
                reflectiveQuadTo(520f, 320f)
                reflectiveQuadToRelative(11.5f, -28.5f)
                reflectiveQuadTo(560f, 280f)
                reflectiveQuadToRelative(28.5f, 11.5f)
                reflectiveQuadTo(600f, 320f)
                reflectiveQuadToRelative(-11.5f, 28.5f)
                reflectiveQuadTo(560f, 360f)
                moveTo(428f, 640f)
                horizontalLineToRelative(264f)
                quadToRelative(-29f, -20f, -63f, -30f)
                reflectiveQuadToRelative(-69f, -10f)
                reflectiveQuadToRelative(-69f, 10f)
                reflectiveQuadToRelative(-63f, 30f)
                moveToRelative(132f, -245f)
            }
        }.build()
        
        return _Switch_account!!
    }

private var _Switch_account: ImageVector? = null

