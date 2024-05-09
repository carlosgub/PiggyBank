package domain.usecase

import core.sealed.GenericState
import domain.repository.FinanceRepository

class DeleteExpenseUseCase(
    private val financeRepository: FinanceRepository,
) {
    suspend operator fun invoke(params: Params): GenericState<Unit> =
        financeRepository.deleteExpense(
            id = params.id,
            monthKey = params.monthKey,
        )

    data class Params(
        val id: Long,
        val monthKey: String,
    )
}
