package com.carlosgub.myfinances.domain.usecase

import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.domain.model.MonthDetailScreenModel
import com.carlosgub.myfinances.domain.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow

class GetExpenseMonthDetailUseCase(
    private val financeRepository: FinanceRepository,
) {
    suspend operator fun invoke(params: Params): Flow<GenericState<MonthDetailScreenModel>> =
        financeRepository.getExpenseMonthDetail(
            categoryEnum = params.categoryEnum,
            monthKey = params.monthKey,
        )

    data class Params(
        val categoryEnum: com.carlosgub.myfinances.domain.model.CategoryEnum,
        val monthKey: String,
    )
}
