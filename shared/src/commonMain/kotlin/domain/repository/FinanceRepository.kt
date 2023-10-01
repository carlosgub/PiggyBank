package domain.repository

import core.sealed.GenericState
import kotlinx.datetime.LocalDateTime
import model.CategoryEnum
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

    suspend fun getCategoryMonthDetail(
        categoryEnum: CategoryEnum,
        monthKey: String
    ): GenericState<MonthDetailScreenModel>

    suspend fun getMonths(): GenericState<Map<Int, List<LocalDateTime>>>
}
