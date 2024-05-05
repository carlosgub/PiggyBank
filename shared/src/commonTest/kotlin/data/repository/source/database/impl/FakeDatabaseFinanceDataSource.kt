package data.repository.source.database.impl

import core.network.ResponseResult
import data.repository.source.database.expenseOne
import data.repository.source.database.expensesList
import data.repository.source.database.incomeList
import data.repository.source.database.incomeOne
import data.repository.source.database.monthList
import data.source.database.DatabaseFinanceDataSource
import domain.model.CategoryEnum
import domain.model.FinanceEnum
import domain.model.MonthModel
import expense.Expense
import income.Income
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDatabaseFinanceDataSource : DatabaseFinanceDataSource {
    override suspend fun getAllMonthExpenses(monthKey: String): Flow<ResponseResult<List<Expense>>> =
        flow {
            emit(
                ResponseResult.Success(
                    expensesList
                )
            )
        }

    override suspend fun getAllMonthIncome(monthKey: String): Flow<ResponseResult<List<Income>>> =
        flow {
            emit(
                ResponseResult.Success(
                    incomeList
                )
            )
        }

    override suspend fun getExpense(id: Long): ResponseResult<Expense> =
        ResponseResult.Success(expenseOne)

    override suspend fun getIncome(id: Long): ResponseResult<Income> =
        ResponseResult.Success(incomeOne)

    override suspend fun createExpense(
        amount: Int,
        category: String,
        note: String,
        dateInMillis: Long
    ): ResponseResult<Unit> =
        ResponseResult.Success(Unit)

    override suspend fun createIncome(
        amount: Int,
        note: String,
        dateInMillis: Long
    ): ResponseResult<Unit> =
        ResponseResult.Success(Unit)

    override suspend fun getExpenseMonthDetail(
        categoryEnum: CategoryEnum,
        monthKey: String
    ): Flow<ResponseResult<List<Expense>>> = flow {
        emit(
            ResponseResult.Success(
                expensesList
            )
        )
    }

    override suspend fun getIncomeMonthDetail(monthKey: String): Flow<ResponseResult<List<Income>>> =
        flow {
            ResponseResult.Success(
                incomeList
            )
        }

    override suspend fun editExpense(
        amount: Long,
        category: String,
        note: String,
        dateInMillis: Long,
        id: Long
    ): ResponseResult<Unit> =
        ResponseResult.Success(Unit)

    override suspend fun editIncome(
        amount: Long,
        note: String,
        dateInMillis: Long,
        id: Long
    ): ResponseResult<Unit> =
        ResponseResult.Success(Unit)

    override suspend fun delete(
        financeEnum: FinanceEnum,
        id: Long,
        monthKey: String
    ): ResponseResult<Unit> =
        ResponseResult.Success(Unit)

    override suspend fun getMonths(): Flow<ResponseResult<List<MonthModel>>> = flow {
        emit(
            ResponseResult.Success(
                monthList
            )
        )
    }
}