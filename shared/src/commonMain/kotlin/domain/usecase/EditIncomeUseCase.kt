package domain.usecase

import domain.repository.FinanceRepository
import presentation.viewmodel.EditSideEffects

class EditIncomeUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend fun editIncome(params: Params): EditSideEffects =
        financeRepository.editIncome(
            amount = params.amount,
            note = params.note,
            dateInMillis = params.dateInMillis,
            id = params.id
        )

    data class Params(
        val amount: Long,
        val note: String,
        val dateInMillis: Long,
        val id: Long
    )
}
