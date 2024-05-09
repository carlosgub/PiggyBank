package data.source.database.expense

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.carlosgub.myfinance.app.Database
import expense.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

fun Database.getExpenseList(month: String): Flow<List<Expense>> = expenseQueries.getExpenseList(month).asFlow().mapToList(Dispatchers.IO)

fun Database.getExpense(id: Long): Expense {
    return expenseQueries.getExpense(id).executeAsOne()
}

fun Database.getExpenseListPerCategory(
    month: String,
    category: String
): Flow<List<Expense>> {
    return expenseQueries.getExpensePerCategoryList(month, category).asFlow().mapToList(Dispatchers.IO)
}

suspend fun Database.createExpense(expense: Expense) {
    expenseQueries.insert(
        id = null,
        amount = expense.amount,
        note = expense.note,
        category = expense.category,
        month = expense.month,
        dateInMillis = expense.dateInMillis
    )
}

suspend fun Database.updateExpense(expense: Expense) {
    expenseQueries.updateExpense(
        id = expense.id,
        amount = expense.amount,
        note = expense.note,
        category = expense.category,
        month = expense.month,
        dateInMillis = expense.dateInMillis
    )
}

suspend fun Database.deleteExpense(id: Long) {
    expenseQueries.delete(
        id = id
    )
}
