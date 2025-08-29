package com.breastcancer.breastcancercare.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.breastcancer.breastcancercare.Res
import com.breastcancer.breastcancercare.default_blog_image
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingSmall
import com.breastcancer.breastcancercare.utils.OverlappingZoomHeaderWithParallax
import com.breastcancer.breastcancercare.utils.text.LoremIpsum
import org.jetbrains.compose.resources.painterResource

@Composable
fun BlogDetailScreen(modifier: Modifier = Modifier.fillMaxSize(), onBack: () -> Unit) {
    OverlappingZoomHeaderWithParallax(
        modifier = modifier,
        header = painterResource(Res.drawable.default_blog_image),
        onBackClick = onBack
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(
                    horizontal = DefaultHorizontalPaddingMedium,
                    vertical = DefaultVerticalPaddingLarge
                ),
                verticalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingSmall)
            ) {
                Text(
                    text = "Blog Title",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
                Text(
                    text = LoremIpsum,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}