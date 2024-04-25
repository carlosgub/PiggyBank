package domain.usecase

import core.sealed.GenericState
import domain.repository.FinanceRepository
import model.FinanceEnum
import model.FinanceModel

class GetOneFinanceUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend operator fun invoke(params: Params): GenericState<FinanceModel> =
        financeRepository.getOneFinance(
            id = params.id,
            financeEnum = params.financeEnum
        )

    data class Params(
        val id: Long,
        val financeEnum: FinanceEnum
    )
}
