package com.carlosgub.myfinances.domain.usecase

import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.domain.model.FinanceModel
import com.carlosgub.myfinances.domain.repository.FinanceRepository

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
