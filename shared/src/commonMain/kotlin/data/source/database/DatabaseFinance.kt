package data.source.database

import core.network.ResponseResult
import data.source.database.expense.Expense
import data.source.database.expense.getExpenseList
import data.source.database.income.Income
import data.source.database.income.getIncomeList
import data.sqldelight.SharedDatabase

class DatabaseFinance constructor(
    private val sharedDatabase: SharedDatabase
) {
    suspend fun getAllMonthExpenses(
        monthKey: String
    ): ResponseResult<List<Expense>> =
        sharedDatabase {
            try {
                ResponseResult.Success(it.getExpenseList(monthKey))
            } catch (e: Exception) {
                ResponseResult.Error(e)
            }
        }


    suspend fun getAllMonthIncome(
        monthKey: String
    ): ResponseResult<List<Income>> =
        sharedDatabase {
            try {
                ResponseResult.Success(it.getIncomeList(monthKey))
            } catch (e: Exception) {
                ResponseResult.Error(e)
            }
        }


}
