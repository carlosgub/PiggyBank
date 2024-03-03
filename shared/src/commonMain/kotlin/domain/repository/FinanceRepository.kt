package domain.repository

import core.sealed.GenericState
import kotlinx.datetime.LocalDateTime
import model.CategoryEnum
import model.FinanceEnum
import model.FinanceScreenModel
import model.MonthDetailScreenModel

interface FinanceRepository {
    suspend fun getFinance(monthKey: String): GenericState<FinanceScreenModel>
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
    ): GenericState<MonthDetailScreenModel>

    suspend fun getIncomeMonthDetail(
        monthKey: String
    ): GenericState<MonthDetailScreenModel>

    suspend fun getMonths(): GenericState<Map<Int, List<LocalDateTime>>>
}
