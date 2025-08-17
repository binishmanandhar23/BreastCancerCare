package com.breastcancer.breastcancercare.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.breastcancer.breastcancercare.models.EventDTO
import com.breastcancer.breastcancercare.models.interfaces.ProgramEventDTO
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPadding
import com.breastcancer.breastcancercare.theme.DefaultVerticalPadding
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.format.char


@OptIn(FormatStringsInDatetimeFormats::class)
@Composable
fun EventProgramDesign(modifier: Modifier, programEventDTO: ProgramEventDTO, onClick: () -> Unit) {
    var finalHeight by remember { mutableStateOf(0) }
    Card(
        modifier = modifier,
        onClick = onClick,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = DefaultHorizontalPadding,
                vertical = DefaultVerticalPadding
            ).onSizeChanged {
                finalHeight = it.height
            },
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = programEventDTO.date.format(LocalDate.Format { dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED) }),
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = programEventDTO.date.format(LocalDate.Format { byUnicodePattern("dd") }),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
            if (programEventDTO is EventDTO)
                Box(
                    modifier = Modifier.height(with(LocalDensity.current) { finalHeight.toDp() })
                        .width(4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = if(programEventDTO.isFeatured) 1f else 0f),
                            shape = MaterialTheme.shapes.medium
                        )
                )
            Column(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row {
                    Text(
                        text = programEventDTO.date.format(LocalDate.Format {
                            monthName(MonthNames.ENGLISH_FULL)
                            char(' ')
                            day()
                        }),
                        style = MaterialTheme.typography.labelSmall
                    )
                    programEventDTO.startTime?.let {
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
                    programEventDTO.endTime?.let {
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
                Text(
                    text = programEventDTO.name,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
                programEventDTO.location?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Text(
                    text = programEventDTO.description,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}