package domain.usecase

import core.sealed.GenericState
import domain.repository.FinanceRepository

class EditIncomeUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend fun editIncome(params: Params): GenericState<Unit> =
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
