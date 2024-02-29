package data.source.database.month

import com.carlosgub.myfinance.app.Database

fun Database.getMonthList(): List<String> {
    return monthQueries.getMonthListList().executeAsList()
}

suspend fun Database.createMonth(month: String) {
    monthQueries.insert(
        month = month
    )
}

suspend fun Database.deleteMonth(month: String) {
    monthQueries.delete(
        month = month
    )
}
