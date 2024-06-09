package domain.usecase

import com.carlosgub.myfinances.core.state.GenericState
import domain.repository.FinanceRepository

class DeleteExpenseUseCase(
    private val financeRepository: FinanceRepository,
) {
    suspend operator fun invoke(params: Params): GenericState<Unit> =
        financeRepository.deleteExpense(
            id = params.id,
            monthKey = params.monthKey,
        )

    data class Params(
        val id: Long,
        val monthKey: String,
    )
}
