package data.source.database.month

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.carlosgub.myfinance.app.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

fun Database.getMonthList(): Flow<List<String>> {
    return monthQueries.getMonthListList().asFlow().mapToList(Dispatchers.IO)
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
