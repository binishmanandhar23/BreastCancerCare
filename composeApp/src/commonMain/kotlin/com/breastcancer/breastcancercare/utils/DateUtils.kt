package com.breastcancer.breastcancercare.utils

import com.breastcancer.breastcancercare.database.local.types.FrequencyType
import com.breastcancer.breastcancercare.models.FrequencySeries
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusDays
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlin.time.ExperimentalTime

fun getDatesFromActivity(
    frequencyType: FrequencyType,
    startDate: LocalDate?,
    endDate: LocalDate?,
    frequencySeries: FrequencySeries?
): List<LocalDate> {
    val endDate = endDate ?: startDate
    val dates = mutableListOf<LocalDate>()
    if (startDate != null && endDate != null)
        when (frequencyType) {
            FrequencyType.Ongoing -> {
                var date = startDate
                var i = 0
                while (date!! <= endDate) {
                    date = startDate.plusDays(i)
                    dates.add(date)
                    i++
                }
            }

            FrequencyType.Weekly -> {
                var date = startDate
                var i = 0
                while (date!! <= endDate) {
                    date = startDate.plus(i, DateTimeUnit.WEEK)
                    dates.add(date)
                    i++
                }
            }

            FrequencyType.Monthly -> {
                var date = startDate
                var i = 0
                while (date!! <= endDate) {
                    date = startDate.plus(i, DateTimeUnit.MONTH)
                    dates.add(date)
                    i++
                }
            }

            FrequencyType.Series -> {
                var date = startDate
                var i = 0
                while (date!! <= endDate) {
                    date = startDate.plus(i, DateTimeUnit.MONTH)
                    val datesInMonth =
                        datesInMonth(
                            year = date.year,
                            month = date.month.number,
                            rules = getRules(frequencySeries = frequencySeries)
                        )
                    dates.addAll(datesInMonth)
                    i++
                }
            }

            else -> Unit
        }
    return dates
}

@OptIn(ExperimentalTime::class)
fun getDateForNextSession(
    frequencyType: FrequencyType,
    startDate: LocalDate?,
    endDate: LocalDate?,
    frequencySeries: FrequencySeries?
): LocalDate? {
    val endDate = endDate ?: startDate
    if (startDate != null && endDate != null)
        when (frequencyType) {
            FrequencyType.OnceOff -> (startDate > LocalDate.now()).let {
                return if (it) startDate else null
            }

            FrequencyType.Ongoing -> {
                var date = startDate
                var i = 0
                while (date!! <= endDate) {
                    date = startDate.plusDays(i)
                    if (date > LocalDate.now())
                        return date
                    i++
                }
            }

            FrequencyType.Weekly -> {
                var date = startDate
                var i = 0
                while (date!! <= endDate) {
                    date = startDate.plus(i, DateTimeUnit.WEEK)
                    if (date > LocalDate.now())
                        return date
                    i++
                }
            }

            FrequencyType.Monthly -> {
                var date = startDate
                var i = 0
                while (date!! <= endDate) {
                    date = startDate.plus(i, DateTimeUnit.MONTH)
                    if (date > LocalDate.now())
                        return date
                    i++
                }
            }

            FrequencyType.Series -> {
                var date = startDate
                var i = 0
                while (date!! <= endDate) {
                    date = startDate.plus(i, DateTimeUnit.MONTH)
                    val datesInMonth =
                        datesInMonth(
                            year = date.year,
                            month = date.month.number,
                            rules = getRules(frequencySeries = frequencySeries)
                        )
                    datesInMonth.sortedBy { it }.find { it > LocalDate.now() }?.let {
                        return it
                    }
                    i++
                }
            }

            FrequencyType.Block -> return null
        }
    return null
}

/** Which occurrence in the month. */
sealed class Occurrence {
    data class Nth(val n: Int) : Occurrence()   // n = 1..5
    data object Last : Occurrence()
}

fun getRules(frequencySeries: FrequencySeries?): List<Pair<DayOfWeek, Occurrence>> =
    if (frequencySeries == null) listOf() else
        listOf(DayOfWeek.valueOf(frequencySeries.dayOfWeek) to Occurrence.Nth(frequencySeries.occurrence))

/** Date of the Nth/Last [dayOfWeek] in a given [year]-[month] (1..12). Returns Last day if n-th doesn’t exist. */
fun dateOfMonthlyOccurrence(
    year: Int,
    month: Int,
    dayOfWeek: DayOfWeek,
    occurrence: Occurrence
): LocalDate? {
    val firstOfMonth = LocalDate(year, month, 1)

    return when (occurrence) {
        is Occurrence.Nth -> {
            require(occurrence.n >= 1) { "n must be >= 1" }
            // offset (0..6) from the 1st to the first desired weekday
            val deltaToFirst =
                (dayOfWeek.isoDayNumber - firstOfMonth.dayOfWeek.isoDayNumber + 7) % 7
            val firstWanted = firstOfMonth.plus(deltaToFirst, DateTimeUnit.DAY)
            val nth = firstWanted.plus((occurrence.n - 1) * 7, DateTimeUnit.DAY)
            if (nth.month.number == month) nth else dateOfMonthlyOccurrence(
                year = year, month = month, dayOfWeek,
                Occurrence.Last
            )
        }

        Occurrence.Last -> {
            // last day = (first of next month) - 1 day
            val firstOfNext = firstOfMonth.plus(1, DateTimeUnit.MONTH)
            val lastOfMonth = firstOfNext.plus(-1, DateTimeUnit.DAY)
            val deltaBack = (lastOfMonth.dayOfWeek.isoDayNumber - dayOfWeek.isoDayNumber + 7) % 7
            lastOfMonth.plus(-deltaBack, DateTimeUnit.DAY)
        }
    }
}

/** All 12 months’ dates for a rule (nulls filtered). */
fun datesForYear(
    year: Int,
    dayOfWeek: DayOfWeek,
    occurrence: Occurrence
): List<LocalDate> =
    (1..12).mapNotNull { m -> dateOfMonthlyOccurrence(year, m, dayOfWeek, occurrence) }

/** Multiple rules in one month (e.g., 2nd Fri, 3rd Tue, …). */
fun datesInMonth(
    year: Int,
    month: Int,
    rules: List<Pair<DayOfWeek, Occurrence>>
): List<LocalDate> =
    rules.mapNotNull { (dow, occ) -> dateOfMonthlyOccurrence(year, month, dow, occ) }
        .sorted()