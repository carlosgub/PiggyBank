package com.carlosgub.myfinances.domain.usecase

import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.domain.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

class GetMonthsUseCase(
    private val financeRepository: FinanceRepository,
) {
    suspend operator fun invoke(): Flow<GenericState<Map<Int, List<LocalDateTime>>>> = financeRepository.getMonths()
}
