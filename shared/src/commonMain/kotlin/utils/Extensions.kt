package utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import domain.model.FinanceEnum
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.roundToInt

fun Double.toMoneyFormat(): String = "$${this.toPrecision(2)}"
fun Float.toMoneyFormat(): String = "$${this.toPrecision(2)}"

fun LocalDateTime.toMonthKey():String = "${this.month.toMonthString()}${this.year}"
fun Float.toPrecision(precision: Int) =
    this.toDouble().toPrecision(precision)

fun Double.toPrecision(precision: Int) =
    if (precision < 1) {
        "${this.roundToInt()}"
    } else {
        val p = 10.0.pow(precision)
        val v = (abs(this) * p).roundToInt()
        val i = floor(v / p)
        var f = "${floor(v - (i * p)).toInt()}"
        while (f.length < precision) f = "0$f"
        val s = if (this < 0) "-" else ""
        "$s${i.toInt()}.$f"
    }

internal fun isLeapYear(year: Int): Boolean {
    val prolepticYear: Long = year.toLong()
    return prolepticYear and 3 == 0L && (prolepticYear % 100 != 0L || prolepticYear % 400 == 0L)
}

internal fun Int.monthLength(isLeapYear: Boolean): Int =
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
    Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.UTC).date

fun LocalDateTime.toMillis(): Long =
    this.toInstant(TimeZone.UTC).toEpochMilliseconds()

fun FinanceEnum.isExpense() = this == FinanceEnum.EXPENSE
