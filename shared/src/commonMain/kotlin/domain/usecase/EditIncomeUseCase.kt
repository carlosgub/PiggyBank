package domain.usecase

import domain.repository.FinanceRepository
import presentation.viewmodel.state.EditState

class EditIncomeUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend fun editIncome(params: Params): EditState =
        financeRepository.editIncome(
            amount = params.amount,
            note = params.note,
            dateInMillis = params.dateInMillis,
            id = params.id
        )

    data class Params(
        val amount: Int,
        val note: String,
        val dateInMillis: Long,
        val id: String
    )
}
