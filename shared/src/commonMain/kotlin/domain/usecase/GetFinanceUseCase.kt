package domain.usecase

import core.sealed.GenericState
import domain.repository.FinanceRepository
import model.FinanceScreenModel

class GetFinanceUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend operator fun invoke(params: Params): GenericState<FinanceScreenModel> =
        financeRepository.getFinance(params.monthKey)

    data class Params(
        val monthKey: String
    )
}
