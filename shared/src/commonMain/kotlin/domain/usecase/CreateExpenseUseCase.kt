package domain.usecase

import com.carlosgub.myfinances.core.state.GenericState
import domain.repository.FinanceRepository

class CreateExpenseUseCase(
    private val financeRepository: FinanceRepository,
) {
    suspend operator fun invoke(params: Params): GenericState<Unit> =
        financeRepository.createExpense(
            amount = params.amount,
            category = params.category,
            note = params.note,
            dateInMillis = params.dateInMillis,
        )

    data class Params(
        val amount: Int,
        val category: String,
        val note: String,
        val dateInMillis: Long,
    )
}
