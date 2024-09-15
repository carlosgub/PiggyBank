package com.carlosgub.myfinances.test.data.repository.impl

import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.domain.model.ExpenseModel
import com.carlosgub.myfinances.domain.model.FinanceModel
import com.carlosgub.myfinances.domain.model.IncomeModel
import com.carlosgub.myfinances.domain.model.MonthDetailModel
import com.carlosgub.myfinances.domain.repository.FinanceRepository
import com.carlosgub.myfinances.test.mock.expenseModelOne
import com.carlosgub.myfinances.test.mock.financeModelMock
import com.carlosgub.myfinances.test.mock.incomeModelOne
import com.carlosgub.myfinances.test.mock.monthExpenseDetailModel
import com.carlosgub.myfinances.test.mock.monthIncomeDetailModel
import com.carlosgub.myfinances.test.mock.monthListFiltered
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime

internal class FakeFinanceRepositoryImpl : FinanceRepository {
    override suspend fun getFinance(monthKey: String): Flow<GenericState<FinanceModel>> =
        flow {
            emit(GenericState.Success(financeModelMock))
        }

    override suspend fun getExpense(id: Long): GenericState<ExpenseModel> = GenericState.Success(expenseModelOne)

    override suspend fun getIncome(id: Long): GenericState<IncomeModel> = GenericState.Success(incomeModelOne)

    override suspend fun createExpense(
        amount: Long,
        category: String,
        note: String,
        dateInMillis: Long,
    ): GenericState<Unit> = GenericState.Success(Unit)

    override suspend fun createIncome(
        amount: Long,
        note: String,
        dateInMillis: Long,
    ): GenericState<Unit> = GenericState.Success(Unit)

    override suspend fun editExpense(
        amount: Long,
        category: String,
        note: String,
        dateInMillis: Long,
        id: Long,
    ): GenericState<Unit> = GenericState.Success(Unit)

    override suspend fun editIncome(
        amount: Long,
        note: String,
        dateInMillis: Long,
        id: Long,
    ): GenericState<Unit> = GenericState.Success(Unit)

    override suspend fun deleteIncome(
        id: Long,
        monthKey: String,
    ): GenericState<Unit> = GenericState.Success(Unit)

    override suspend fun deleteExpense(
        id: Long,
        monthKey: String,
    ): GenericState<Unit> = GenericState.Success(Unit)

    override suspend fun getExpenseMonthDetail(
        categoryEnum: com.carlosgub.myfinances.domain.model.CategoryEnum,
        monthKey: String,
    ): Flow<GenericState<MonthDetailModel>> =
        flow {
            emit(
                GenericState.Success(monthExpenseDetailModel),
            )
        }

    override suspend fun getIncomeMonthDetail(monthKey: String): Flow<GenericState<MonthDetailModel>> =
        flow {
            emit(
                GenericState.Success(monthIncomeDetailModel),
            )
        }

    override suspend fun getMonths(): Flow<GenericState<ImmutableMap<Int, List<LocalDateTime>>>> =
        flow {
            emit(
                GenericState.Success(monthListFiltered),
            )
        }
}
