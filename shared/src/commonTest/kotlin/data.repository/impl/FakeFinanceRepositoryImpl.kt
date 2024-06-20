package data.repository.impl

import com.carlosgub.myfinances.core.state.GenericState
import domain.model.CategoryEnum
import domain.model.FinanceModel
import domain.model.FinanceScreenModel
import domain.model.MonthDetailScreenModel
import domain.repository.FinanceRepository
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime
import mock.expenseFinanceModelOne
import mock.financeScreenModel
import mock.incomeFinanceModelOne
import mock.monthExpenseDetailScreenModel
import mock.monthIncomeDetailScreenModel
import mock.monthListFiltered

class FakeFinanceRepositoryImpl : FinanceRepository {
    override suspend fun getFinance(monthKey: String): Flow<GenericState<FinanceScreenModel>> =
        flow {
            emit(GenericState.Success(financeScreenModel))
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
        categoryEnum: CategoryEnum,
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
