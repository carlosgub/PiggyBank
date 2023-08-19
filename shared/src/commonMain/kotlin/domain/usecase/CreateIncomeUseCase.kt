package domain.usecase

import core.sealed.GenericState
import domain.repository.FinanceRepository

class CreateIncomeUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend fun createIncome(params: Params): GenericState<Unit> =
        financeRepository.createIncome(
            params.amount,
            params.note
        )

    data class Params(
        val amount: Int,
        val note: String
    )
}
