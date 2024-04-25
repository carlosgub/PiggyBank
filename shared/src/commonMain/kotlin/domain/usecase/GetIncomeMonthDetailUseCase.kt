package domain.usecase

import core.sealed.GenericState
import domain.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow
import model.MonthDetailScreenModel

class GetIncomeMonthDetailUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend operator fun invoke(params: Params): Flow<GenericState<MonthDetailScreenModel>> =
        financeRepository.getIncomeMonthDetail(
            monthKey = params.monthKey
        )

    data class Params(
        val monthKey: String
    )
}
