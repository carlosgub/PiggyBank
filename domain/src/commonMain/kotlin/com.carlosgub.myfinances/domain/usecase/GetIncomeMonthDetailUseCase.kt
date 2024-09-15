package com.carlosgub.myfinances.domain.usecase

import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.domain.model.MonthDetailModel
import com.carlosgub.myfinances.domain.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow

class GetIncomeMonthDetailUseCase(
    private val financeRepository: FinanceRepository,
) {
    suspend operator fun invoke(params: Params): Flow<GenericState<MonthDetailModel>> =
        financeRepository.getIncomeMonthDetail(
            monthKey = params.monthKey,
        )

    data class Params(
        val monthKey: String,
    )
}
