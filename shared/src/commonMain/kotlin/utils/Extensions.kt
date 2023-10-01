package utils

import kotlinx.datetime.Month
import kotlinx.datetime.number
import model.CreateEnum
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.roundToInt

fun Double.toMoneyFormat(): String = "$${this.toPrecision(2)}"
fun Float.toMoneyFormat(): String = "$${this.toPrecision(2)}"

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
        this.number.numberToTwoDigits()
    } else {
        this.number
    }

fun Int.toDayString() =
    if (this < 10) {
        this.numberToTwoDigits()
    } else {
        this
    }

fun Int.numberToTwoDigits() =
    if (this < 10) {
        "0${this}"
    } else {
        this
    }

fun CreateEnum.isExpense() = this == CreateEnum.EXPENSE
