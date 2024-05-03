package data.source.database.income

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.carlosgub.myfinance.app.Database
import income.Income
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

fun Database.getIncomeList(month: String): Flow<List<Income>> = incomeQueries.getIncomeList(month).asFlow().mapToList(Dispatchers.IO)

fun Database.getIncome(id: Long): Income {
    return incomeQueries.getIncome(id).executeAsOne()
}

fun Database.getIncomeListPerCategory(
    month: String,
    category: String,
): Flow<List<Income>> {
    return incomeQueries.getIncomePerCategoryList(month, category).asFlow()
        .mapToList(Dispatchers.IO)
}

suspend fun Database.createIncome(income: Income) {
    incomeQueries.insert(
        id = null,
        amount = income.amount,
        note = income.note,
        category = income.category,
        month = income.month,
        dateInMillis = income.dateInMillis,
    )
}

suspend fun Database.updateIncome(income: Income) {
    incomeQueries.updateIncome(
        id = income.id,
        amount = income.amount,
        note = income.note,
        category = income.category,
        month = income.month,
        dateInMillis = income.dateInMillis,
    )
}

suspend fun Database.deleteIncome(id: Long) {
    incomeQueries.delete(
        id = id,
    )
}
