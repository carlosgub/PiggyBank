package data.source.database.expense

import com.carlosgub.myfinance.app.Database
import expense.Expense

fun Database.getExpenseList(month: String): List<Expense> {
    return expenseQueries.getExpenseList(month).executeAsList()
}

fun Database.getExpenseListPerCategory(month: String, category: String): List<Expense> {
    return expenseQueries.getExpensePerCategoryList(month, category).executeAsList()
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
