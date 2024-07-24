package com.carlosgub.myfinances.test.data.repository.database

import com.carlosgub.myfinances.core.network.ResponseResult
import com.carlosgub.myfinances.data.database.DatabaseFinanceDataSource
import com.carlosgub.myfinances.domain.model.CategoryEnum.Companion.getCategoryEnumFromName
import com.carlosgub.myfinances.domain.model.MonthModel
import com.carlosgub.myfinances.test.mock.expenseOne
import com.carlosgub.myfinances.test.mock.expensesList
import com.carlosgub.myfinances.test.mock.incomeList
import com.carlosgub.myfinances.test.mock.incomeOne
import com.carlosgub.myfinances.test.mock.monthList
import expense.Expense
import income.Income
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDatabaseFinanceDataSource : DatabaseFinanceDataSource {
    override suspend fun getAllMonthExpenses(monthKey: String): Flow<ResponseResult<List<Expense>>> =
        flow {
            emit(
                ResponseResult.Success(
                    expensesList,
                ),
            )
        }

    override suspend fun getAllMonthIncome(monthKey: String): Flow<ResponseResult<List<Income>>> =
        flow {
            emit(
                ResponseResult.Success(
                    incomeList,
                ),
            )
        }

    override suspend fun getExpense(id: Long): ResponseResult<Expense> = ResponseResult.Success(expenseOne)

    override suspend fun getIncome(id: Long): ResponseResult<Income> = ResponseResult.Success(incomeOne)

    override suspend fun createExpense(
        amount: Long,
        category: String,
        note: String,
        dateInMillis: Long,
    ): ResponseResult<Unit> = ResponseResult.Success(Unit)

    override suspend fun createIncome(
        amount: Long,
        note: String,
        dateInMillis: Long,
    ): ResponseResult<Unit> = ResponseResult.Success(Unit)

    override suspend fun getExpenseMonthDetail(
        categoryEnum: com.carlosgub.myfinances.domain.model.CategoryEnum,
        monthKey: String,
    ): Flow<ResponseResult<List<Expense>>> =
        flow {
            emit(
                ResponseResult.Success(
                    expensesList.filter {
                        getCategoryEnumFromName(it.category) == categoryEnum
                    },
                ),
            )
        }

    override suspend fun getIncomeMonthDetail(monthKey: String): Flow<ResponseResult<List<Income>>> =
        flow {
            emit(
                ResponseResult.Success(incomeList),
            )
        }

    override suspend fun editExpense(
        amount: Long,
        category: String,
        note: String,
        dateInMillis: Long,
        id: Long,
    ): ResponseResult<Unit> = ResponseResult.Success(Unit)

    override suspend fun editIncome(
        amount: Long,
        note: String,
        dateInMillis: Long,
        id: Long,
    ): ResponseResult<Unit> = ResponseResult.Success(Unit)

    override suspend fun deleteExpense(
        id: Long,
        monthKey: String,
    ): ResponseResult<Unit> = ResponseResult.Success(Unit)

    override suspend fun deleteIncome(
        id: Long,
        monthKey: String,
    ): ResponseResult<Unit> = ResponseResult.Success(Unit)

    override suspend fun getMonths(): Flow<ResponseResult<List<MonthModel>>> =
        flow {
            emit(
                ResponseResult.Success(
                    monthList,
                ),
            )
        }
}
