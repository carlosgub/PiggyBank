package data.source.database.income

import com.carlosgub.myfinance.app.Database

fun Database.getIncomeList(month: String): List<Income> {
    return incomeQueries.getIncomeList(month).executeAsList()
}

fun Database.getIncomeListPerCategory(month: String, category: String): List<Income> {
    return incomeQueries.getIncomePerCategoryList(month, category).executeAsList()
}

fun Database.createIncome(income: Income) {
    incomeQueries.insert(
        id = null,
        amount = income.amount,
        note = income.note,
        category = income.category,
        month = income.month,
        dateInMillis = income.dateInMillis
    )
}

fun Database.updateIncome(income: Income) {
    incomeQueries.updateIncome(
        id = income.id,
        amount = income.amount,
        note = income.note,
        category = income.category,
        month = income.month,
        dateInMillis = income.dateInMillis
    )
}

fun Database.deleteIncome(id: Long) {
    incomeQueries.delete(
        id = id
    )
}
