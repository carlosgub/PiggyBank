package domain.usecase

import core.sealed.GenericState
import domain.repository.FinanceRepository
import model.Finance

class GetFinanceUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend fun getFinance(): GenericState<Finance> =
        financeRepository.getFinance()
}
