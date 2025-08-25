package com.breastcancer.breastcancercare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import com.breastcancer.breastcancercare.theme.DarkAppColorScheme
import com.breastcancer.breastcancercare.theme.LightAppColorScheme
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.rememberNavigator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val darkTheme = isSystemInDarkTheme()
            MaterialTheme(
                colorScheme = if (darkTheme) LightAppColorScheme else LightAppColorScheme,
            ) {
                PreComposeApp {
                    App()
                }
            }
        }
    }
}