package domain.usecase

import core.sealed.GenericState
import domain.repository.FinanceRepository

class DeleteIncomeUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend operator fun invoke(params: Params): GenericState<Unit> =
        financeRepository.deleteIncome(
            id = params.id,
            monthKey = params.monthKey
        )

    data class Params(
        val id: Long,
        val monthKey: String
    )
}
