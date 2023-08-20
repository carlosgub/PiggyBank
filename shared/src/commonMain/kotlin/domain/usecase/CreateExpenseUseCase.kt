package domain.usecase

import core.sealed.GenericState
import domain.repository.FinanceRepository

class CreateExpenseUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend fun createFinance(params: Params): GenericState<Unit> =
        financeRepository.createExpense(
            params.amount,
            params.category,
            params.note
        )

    data class Params(
        val amount: Int,
        val category: String,
        val note: String
    )
}
