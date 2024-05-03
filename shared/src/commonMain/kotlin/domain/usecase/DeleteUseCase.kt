package domain.usecase

import core.sealed.GenericState
import domain.model.FinanceEnum
import domain.repository.FinanceRepository

class DeleteUseCase(
    private val financeRepository: FinanceRepository,
) {
    suspend operator fun invoke(params: Params): GenericState<Unit> =
        financeRepository.delete(
            financeEnum = params.financeEnum,
            id = params.id,
            monthKey = params.monthKey,
        )

    data class Params(
        val financeEnum: FinanceEnum,
        val id: Long,
        val monthKey: String,
    )
}
