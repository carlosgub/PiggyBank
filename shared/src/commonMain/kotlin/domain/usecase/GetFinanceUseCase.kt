package domain.usecase

import core.sealed.GenericState
import domain.repository.FinanceRepository
import model.FinanceScreenModel

class GetFinanceUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend fun getFinance(): GenericState< FinanceScreenModel> =
        financeRepository.getCurrentFinance()
}
