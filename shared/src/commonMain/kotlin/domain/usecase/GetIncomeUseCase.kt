package domain.usecase

import core.sealed.GenericState
import domain.model.FinanceModel
import domain.repository.FinanceRepository

class GetIncomeUseCase(
    private val financeRepository: FinanceRepository,
) {
    suspend operator fun invoke(params: Params): GenericState<FinanceModel> =
        financeRepository.getIncome(
            id = params.id,
        )

    data class Params(
        val id: Long,
    )
}
