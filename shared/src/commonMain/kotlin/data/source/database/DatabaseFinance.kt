package data.source.database

import core.network.ResponseResult
import data.source.database.expense.Expense
import data.source.database.expense.createExpense
import data.source.database.expense.getExpenseList
import data.source.database.expense.getExpenseListPerCategory
import data.source.database.income.Income
import data.source.database.income.createIncome
import data.source.database.income.getIncomeList
import data.source.database.income.getIncomeListPerCategory
import data.sqldelight.SharedDatabase
import kotlinx.datetime.LocalDate
import model.CategoryEnum
import model.FinanceEnum
import utils.COLLECTION_EXPENSE
import utils.COLLECTION_INCOME
import utils.toLocalDate
import utils.toMonthString

class DatabaseFinance constructor(
    private val sharedDatabase: SharedDatabase
) {
    suspend fun getAllMonthExpenses(
        monthKey: String
    ): ResponseResult<List<Expense>> =
        try {
            ResponseResult.Success(sharedDatabase().getExpenseList(monthKey))
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }


    suspend fun getAllMonthIncome(
        monthKey: String
    ): ResponseResult<List<Income>> =
        try {
            ResponseResult.Success(sharedDatabase().getIncomeList(monthKey))
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    suspend fun createExpense(
        amount: Int,
        category: String,
        note: String,
        dateInMillis: Long
    ): ResponseResult<Unit> =
        try {
            val date: LocalDate = dateInMillis.toLocalDate()
            val currentMonthKey = "${date.month.toMonthString()}${date.year}"
            sharedDatabase().createExpense(
                Expense(
                    id = 1,
                    amount = amount.toLong(),
                    category = category,
                    note = note,
                    dateInMillis = dateInMillis,
                    month = currentMonthKey
                )
            )
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    suspend fun createIncome(
        amount: Int,
        note: String,
        dateInMillis: Long
    ): ResponseResult<Unit> =
        try {
            val date: LocalDate = dateInMillis.toLocalDate()
            val currentMonthKey = "${date.month.toMonthString()}${date.year}"
            sharedDatabase().createIncome(
                Income(
                    id = 1,
                    amount = amount.toLong(),
                    category = CategoryEnum.WORK.name,
                    note = note,
                    dateInMillis = dateInMillis,
                    month = currentMonthKey
                )
            )
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    suspend fun getCategoryMonthDetail(
        categoryEnum: CategoryEnum,
        monthKey: String
    ): ResponseResult<List<Expense>> =
        try {
            val list = sharedDatabase().getExpenseListPerCategory(
                month = monthKey,
                category = categoryEnum.name
            )
            ResponseResult.Success(list)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }
}
