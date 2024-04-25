package domain.usecase

import core.sealed.GenericState
import domain.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

class GetMonthsUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend operator fun invoke(): Flow<GenericState<Map<Int, List<LocalDateTime>>>> =
        financeRepository.getMonths()
}
