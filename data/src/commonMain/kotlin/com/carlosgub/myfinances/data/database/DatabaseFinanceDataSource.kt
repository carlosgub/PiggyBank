package com.carlosgub.myfinances.data.database

import com.carlosgub.myfinances.core.network.ResponseResult
import com.carlosgub.myfinances.domain.model.MonthModel
import expense.Expense
import income.Income
import kotlinx.coroutines.flow.Flow

interface DatabaseFinanceDataSource {
    suspend fun getAllMonthExpenses(monthKey: String): Flow<ResponseResult<List<Expense>>>

    suspend fun getAllMonthIncome(monthKey: String): Flow<ResponseResult<List<Income>>>

    suspend fun getExpense(id: Long): ResponseResult<Expense>

    suspend fun getIncome(id: Long): ResponseResult<Income>

    suspend fun createExpense(
        amount: Int,
        category: String,
        note: String,
        dateInMillis: Long,
    ): ResponseResult<Unit>

    suspend fun createIncome(
        amount: Int,
        note: String,
        dateInMillis: Long,
    ): ResponseResult<Unit>

    suspend fun getExpenseMonthDetail(
        categoryEnum: com.carlosgub.myfinances.domain.model.CategoryEnum,
        monthKey: String,
    ): Flow<ResponseResult<List<Expense>>>

    suspend fun getIncomeMonthDetail(monthKey: String): Flow<ResponseResult<List<Income>>>

    suspend fun editExpense(
        amount: Long,
        category: String,
        note: String,
        dateInMillis: Long,
        id: Long,
    ): ResponseResult<Unit>

    suspend fun editIncome(
        amount: Long,
        note: String,
        dateInMillis: Long,
        id: Long,
    ): ResponseResult<Unit>

    suspend fun deleteIncome(
        id: Long,
        monthKey: String,
    ): ResponseResult<Unit>

    suspend fun deleteExpense(
        id: Long,
        monthKey: String,
    ): ResponseResult<Unit>

    suspend fun getMonths(): Flow<ResponseResult<List<MonthModel>>>
}
