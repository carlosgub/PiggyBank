package domain.usecase

import com.carlosgub.myfinances.core.state.GenericState
import domain.repository.FinanceRepository

class EditExpenseUseCase(
    private val financeRepository: FinanceRepository,
) {
    suspend operator fun invoke(params: Params): GenericState<Unit> =
        financeRepository.editExpense(
            amount = params.amount,
            category = params.category,
            note = params.note,
            dateInMillis = params.dateInMillis,
            id = params.id,
        )

    data class Params(
        val amount: Long,
        val category: String,
        val note: String,
        val dateInMillis: Long,
        val id: Long,
    )
}
