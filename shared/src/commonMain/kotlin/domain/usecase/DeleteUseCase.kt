package domain.usecase

import domain.repository.FinanceRepository
import model.FinanceEnum
import presentation.viewmodel.state.EditState

class DeleteUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend fun delete(params: Params): EditState =
        financeRepository.delete(
            financeEnum = params.financeEnum,
            id = params.id
        )

    data class Params(
        val financeEnum: FinanceEnum,
        val id: String
    )
}
