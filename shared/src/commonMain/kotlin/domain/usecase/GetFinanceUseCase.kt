package domain.usecase

import core.sealed.GenericState
import domain.model.FinanceScreenModel
import domain.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow

class GetFinanceUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend operator fun invoke(params: Params): Flow<GenericState<FinanceScreenModel>> = financeRepository.getFinance(params.monthKey)

    data class Params(
        val monthKey: String
    )
}
