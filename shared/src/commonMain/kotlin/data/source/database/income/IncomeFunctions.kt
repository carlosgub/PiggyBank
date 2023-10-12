package data.source.database.income

import com.carlosgub.myfinance.app.Database

fun Database.getIncomeList(month: String): List<Income> {
    return incomeQueries.getIncomeList(month).executeAsList()
}

fun Database.createExpense(income: Income) {
    incomeQueries.insert(
        id = null,
        amount = income.amount,
        note = income.note,
        category = income.category,
        month = income.month,
        dateInMillis = income.dateInMillis
    )
}

fun Database.updateExpense(income: Income) {
    incomeQueries.updateIncome(
        id = income.id,
        amount = income.amount,
        note = income.note,
        category = income.category,
        month = income.month,
        dateInMillis = income.dateInMillis
    )
}