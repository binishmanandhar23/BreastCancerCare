package com.breastcancer.breastcancercare.screens.main.survey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.breastcancer.breastcancercare.components.BreastCancerButton
import com.breastcancer.breastcancercare.components.icons.InfoCircle
import com.breastcancer.breastcancercare.theme.ColorOrchid
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium

@Composable
fun SurveyMandatoryDialogScreen(onProceed: () -> Unit) {
    Card(modifier = Modifier, shape = MaterialTheme.shapes.large) {
        Column(
            modifier = Modifier.padding(
                horizontal = DefaultHorizontalPaddingLarge,
                vertical = DefaultVerticalPaddingLarge
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                DefaultVerticalPaddingMedium
            )
        ) {
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = InfoCircle,
                contentDescription = "Information",
                tint = ColorOrchid
            )
            Text(
                text = "Important Information before Registering",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            Text(
                text = "Before you can proceed to registering there is a mandatory survey you need to complete.",
                style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center
            )
            BreastCancerButton(text = "Ok", onClick = onProceed)
        }
    }
}