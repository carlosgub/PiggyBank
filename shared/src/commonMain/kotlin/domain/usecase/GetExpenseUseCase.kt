package domain.usecase

import core.sealed.GenericState
import domain.repository.FinanceRepository
import domain.model.FinanceEnum
import domain.model.FinanceModel

class GetExpenseUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend operator fun invoke(params: Params): GenericState<FinanceModel> =
        financeRepository.getExpense(
            id = params.id
        )

    data class Params(
        val id: Long
    )
}
