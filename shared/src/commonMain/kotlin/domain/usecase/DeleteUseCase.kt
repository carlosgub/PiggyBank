package domain.usecase

import core.sealed.GenericState
import domain.repository.FinanceRepository
import model.FinanceEnum

class DeleteUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend fun delete(params: Params): GenericState<Unit> =
        financeRepository.delete(
            financeEnum = params.financeEnum,
            id = params.id,
            monthKey = params.monthKey
        )

    data class Params(
        val financeEnum: FinanceEnum,
        val id: Long,
        val monthKey: String
    )
}
