package domain.repository

import core.sealed.GenericState
import model.Finance
import model.FinanceScreenModel

interface FinanceRepository {
    suspend fun getCurrentFinance(): GenericState<FinanceScreenModel>
    suspend fun createExpense(
        amount: Int,
        category: String,
        note: String
    ): GenericState<Unit>
}
