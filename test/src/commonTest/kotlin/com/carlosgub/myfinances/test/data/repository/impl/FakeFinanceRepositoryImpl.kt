package com.carlosgub.myfinances.test.data.repository.impl

import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.domain.model.FinanceModel
import com.carlosgub.myfinances.domain.model.FinanceScreenModel
import com.carlosgub.myfinances.domain.model.MonthDetailScreenModel
import com.carlosgub.myfinances.domain.repository.FinanceRepository
import com.carlosgub.myfinances.test.mock.expenseFinanceModelOne
import com.carlosgub.myfinances.test.mock.financeScreenModelMock
import com.carlosgub.myfinances.test.mock.incomeFinanceModelOne
import com.carlosgub.myfinances.test.mock.monthExpenseDetailScreenModel
import com.carlosgub.myfinances.test.mock.monthIncomeDetailScreenModel
import com.carlosgub.myfinances.test.mock.monthListFiltered
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime

internal class FakeFinanceRepositoryImpl : FinanceRepository {
    override suspend fun getFinance(monthKey: String): Flow<GenericState<FinanceScreenModel>> =
        flow {
            emit(GenericState.Success(financeScreenModelMock))
        }

    override suspend fun getExpense(id: Long): GenericState<FinanceModel> = GenericState.Success(expenseFinanceModelOne)

    override suspend fun getIncome(id: Long): GenericState<FinanceModel> = GenericState.Success(incomeFinanceModelOne)

    override suspend fun createExpense(
        amount: Int,
        category: String,
        note: String,
        dateInMillis: Long,
    ): GenericState<Unit> = GenericState.Success(Unit)

    override suspend fun createIncome(
        amount: Int,
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
    ): Flow<GenericState<MonthDetailScreenModel>> =
        flow {
            emit(
                GenericState.Success(monthExpenseDetailScreenModel),
            )
        }

    override suspend fun getIncomeMonthDetail(monthKey: String): Flow<GenericState<MonthDetailScreenModel>> =
        flow {
            emit(
                GenericState.Success(monthIncomeDetailScreenModel),
            )
        }

    override suspend fun getMonths(): Flow<GenericState<ImmutableMap<Int, List<LocalDateTime>>>> =
        flow {
            emit(
                GenericState.Success(monthListFiltered),
            )
        }
}
