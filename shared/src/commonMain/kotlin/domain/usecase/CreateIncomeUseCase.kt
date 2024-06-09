package domain.usecase

import com.carlosgub.myfinances.core.state.GenericState
import domain.repository.FinanceRepository

class CreateIncomeUseCase(
    private val financeRepository: FinanceRepository,
) {
    suspend operator fun invoke(params: Params): GenericState<Unit> =
        financeRepository.createIncome(
            amount = params.amount,
            note = params.note,
            dateInMillis = params.dateInMillis,
        )

    data class Params(
        val amount: Int,
        val note: String,
        val dateInMillis: Long,
    )
}
