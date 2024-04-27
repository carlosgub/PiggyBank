package domain.repository

import core.sealed.GenericState
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import domain.model.CategoryEnum
import domain.model.FinanceEnum
import domain.model.FinanceModel
import domain.model.FinanceScreenModel
import domain.model.MonthDetailScreenModel

interface FinanceRepository {
    suspend fun getFinance(monthKey: String): Flow<GenericState<FinanceScreenModel>>
    suspend fun getExpense(
        id: Long
    ): GenericState<FinanceModel>

    suspend fun getIncome(
        id: Long
    ): GenericState<FinanceModel>

    suspend fun createExpense(
        amount: Int,
        category: String,
        note: String,
        dateInMillis: Long
    ): GenericState<Unit>

    suspend fun createIncome(
        amount: Int,
        note: String,
        dateInMillis: Long
    ): GenericState<Unit>

    suspend fun editExpense(
        amount: Long,
        category: String,
        note: String,
        dateInMillis: Long,
        id: Long
    ): GenericState<Unit>

    suspend fun editIncome(
        amount: Long,
        note: String,
        dateInMillis: Long,
        id: Long
    ): GenericState<Unit>

    suspend fun delete(
        financeEnum: FinanceEnum,
        id: Long,
        monthKey: String
    ): GenericState<Unit>

    suspend fun getExpenseMonthDetail(
        categoryEnum: CategoryEnum,
        monthKey: String
    ): Flow<GenericState<MonthDetailScreenModel>>

    suspend fun getIncomeMonthDetail(
        monthKey: String
    ): Flow<GenericState<MonthDetailScreenModel>>

    suspend fun getMonths(): Flow<GenericState<Map<Int, List<LocalDateTime>>>>
}
