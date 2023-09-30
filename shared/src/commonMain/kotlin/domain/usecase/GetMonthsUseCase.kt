package domain.usecase

import core.sealed.GenericState
import domain.repository.FinanceRepository
import model.FinanceScreenModel
import model.MonthModel

class GetMonthsUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend fun getMonths(): GenericState<List<MonthModel>> =
        financeRepository.getMonths()
}
