package domain.repository

import core.sealed.GenericState
import model.CategoryEnum
import data.model.Expense
import model.FinanceScreenModel
import model.MonthDetailScreenModel

interface FinanceRepository {
    suspend fun getCurrentFinance(): GenericState<FinanceScreenModel>
    suspend fun createExpense(
        amount: Int,
        category: String,
        note: String
    ): GenericState<Unit>
    suspend fun createIncome(
        amount: Int,
        note: String
    ): GenericState<Unit>
    suspend fun getCategoryMonthDetail(
        categoryEnum: CategoryEnum,
        monthKey: String
    ): GenericState<MonthDetailScreenModel>
}
