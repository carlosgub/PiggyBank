package domain.usecase

import domain.repository.FinanceRepository
import presentation.viewmodel.state.EditState

class EditExpenseUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend fun editExpense(params: Params): EditState =
        financeRepository.editExpense(
            amount = params.amount,
            category = params.category,
            note = params.note,
            dateInMillis = params.dateInMillis,
            id = params.id
        )

    data class Params(
        val amount: Long,
        val category: String,
        val note: String,
        val dateInMillis: Long,
        val id: Long
    )
}
