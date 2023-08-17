package domain.repository

import core.sealed.GenericState
import model.Finance

interface FinanceRepository {
    suspend fun getFinance(): GenericState<Finance>
    suspend fun createExpense(
        amount: Int,
        category: String,
        note: String
    ): GenericState<Unit>
}
