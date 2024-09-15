package com.carlosgub.myfinances.core.utils

import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.Month.APRIL
import kotlinx.datetime.Month.AUGUST
import kotlinx.datetime.Month.DECEMBER
import kotlinx.datetime.Month.FEBRUARY
import kotlinx.datetime.Month.JANUARY
import kotlinx.datetime.Month.JULY
import kotlinx.datetime.Month.JUNE
import kotlinx.datetime.Month.MARCH
import kotlinx.datetime.Month.MAY
import kotlinx.datetime.Month.NOVEMBER
import kotlinx.datetime.Month.OCTOBER
import kotlinx.datetime.Month.SEPTEMBER
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.roundToInt

fun Long.toMoneyFormat(): String =
    when {
        this < 10 -> {
            "$0.0$this"
        }

        this < 100 -> {
            "$0.$this"
        }

        else -> {
            val numberString = this.toString()
            "$${numberString.substring(0, numberString.length - 2)}.${numberString.substring(numberString.length - 2, numberString.length)}"
        }
    }

fun String.toAmount(): Long {
    val cleanString: String =
        this.replace("""[$,.A-Za-z]""".toRegex(), "").trim().trimStart('0')
    return if (cleanString.isBlank()) {
        0L
    } else {
        cleanString.toLongOrNull() ?: Long.MAX_VALUE
    }
}

fun Float.toMoneyFormat(): String {
    val p = 10.0.pow(2)
    val v = (abs(this) * p).roundToInt()
    val i = floor(v / p)
    var f = "${floor(v - (i * p)).toInt()}"
    while (f.length < 2) f = "0$f"
    val s = if (this < 0) "-" else ""
    return "$$s${i.toInt()}.$f"
}

fun LocalDateTime.toMonthKey(): String = "${this.month.toMonthString()}${this.year}"

fun isLeapYear(year: Int): Boolean {
    val prolepticYear: Long = year.toLong()
    return prolepticYear and 3 == 0L && (prolepticYear % 100 != 0L || prolepticYear % 400 == 0L)
}

fun Int.monthLength(isLeapYear: Boolean): Int =
    when (this) {
        2 -> if (isLeapYear) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }

fun Month.toMonthString() =
    if (this.number < 10) {
        this.number.toNumberOfTwoDigits()
    } else {
        this.number
    }

fun Int.toDayString() =
    if (this < 10) {
        this.toNumberOfTwoDigits()
    } else {
        this
    }

fun Int.toNumberOfTwoDigits() =
    if (this < 10) {
        "0$this"
    } else {
        this
    }

fun Long.toLocalDate(): LocalDate =
    Instant
        .fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.UTC)
        .date

fun LocalDateTime.toMillis(): Long = this.toInstant(TimeZone.UTC).toEpochMilliseconds()

fun Long.toStringDateFormat(): String {
    val localDate = this.toLocalDate()
    return "${localDate.dayOfMonth.toNumberOfTwoDigits()}/" +
        "${localDate.monthNumber.toNumberOfTwoDigits()}/" +
        "${localDate.year}"
}

fun Month.toLocaleString(): String =
    if (Locale.current.language.contains("es")) {
        this.toSpanishLocale()
    } else {
        this.name
    }

private fun Month.toSpanishLocale(): String =
    when (this) {
        JANUARY -> "ENERO"
        FEBRUARY -> "FEBRERO"
        MARCH -> "MARZO"
        APRIL -> "ABRIL"
        MAY -> "MAYO"
        JUNE -> "JUNIO"
        JULY -> "JULIO"
        AUGUST -> "AGOSTO"
        SEPTEMBER -> "SEPTIEMBRE"
        OCTOBER -> "OCTUBRE"
        NOVEMBER -> "NOVIEMBRE"
        DECEMBER -> "DICIEMBRE"
        else -> ""
    }
