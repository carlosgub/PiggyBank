package com.carlosgub.myfinances.domain.usecase

import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.domain.model.ExpenseModel
import com.carlosgub.myfinances.domain.repository.FinanceRepository

class GetExpenseUseCase(
    private val financeRepository: FinanceRepository,
) {
    suspend operator fun invoke(params: Params): GenericState<ExpenseModel> =
        financeRepository.getExpense(
            id = params.id,
        )

    data class Params(
        val id: Long,
    )
}
