package domain.repository

import core.network.ResponseResult
import core.sealed.GenericState
import model.CategoryEnum
import model.Expense
import model.FinanceScreenModel

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
    ): GenericState<List<Expense>>
}
