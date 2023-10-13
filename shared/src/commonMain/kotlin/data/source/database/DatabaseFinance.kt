package data.source.database

import core.network.ResponseResult
import data.source.database.expense.Expense
import data.source.database.expense.createExpense
import data.source.database.expense.deleteExpense
import data.source.database.expense.getExpenseList
import data.source.database.expense.getExpenseListPerCategory
import data.source.database.expense.updateExpense
import data.source.database.income.Income
import data.source.database.income.createIncome
import data.source.database.income.deleteIncome
import data.source.database.income.getIncomeList
import data.source.database.income.getIncomeListPerCategory
import data.source.database.income.updateIncome
import data.sqldelight.SharedDatabase
import kotlinx.datetime.LocalDate
import model.CategoryEnum
import model.FinanceEnum
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

    suspend fun getExpenseMonthDetail(
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

    suspend fun getIncomeMonthDetail(
        monthKey: String
    ): ResponseResult<List<Income>> =
        try {
            val list = sharedDatabase().getIncomeListPerCategory(
                month = monthKey,
                category = CategoryEnum.WORK.name
            )
            ResponseResult.Success(list)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    suspend fun editExpense(
        amount: Long,
        category: String,
        note: String,
        dateInMillis: Long,
        id: Long
    ): ResponseResult<Unit> =
        try {
            val date: LocalDate = dateInMillis.toLocalDate()
            val currentMonthKey = "${date.month.toMonthString()}${date.year}"
            sharedDatabase().updateExpense(
                Expense(
                    id = id,
                    note = note,
                    category = category,
                    amount = amount,
                    dateInMillis = dateInMillis,
                    month = currentMonthKey
                )
            )
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    suspend fun editIncome(
        amount: Long,
        note: String,
        dateInMillis: Long,
        id: Long
    ): ResponseResult<Unit> =
        try {
            val date: LocalDate = dateInMillis.toLocalDate()
            val currentMonthKey = "${date.month.toMonthString()}${date.year}"
            sharedDatabase().updateIncome(
                Income(
                    id = id,
                    note = note,
                    category = CategoryEnum.WORK.name,
                    amount = amount,
                    dateInMillis = dateInMillis,
                    month = currentMonthKey
                )
            )
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    suspend fun delete(
        financeEnum: FinanceEnum,
        id: Long
    ): ResponseResult<Unit> =
        try {
            if (financeEnum == FinanceEnum.EXPENSE) {
                sharedDatabase().deleteExpense(id)
            } else {
                sharedDatabase().deleteIncome(id)
            }
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }
}
