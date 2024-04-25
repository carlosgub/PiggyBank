package domain.usecase

import core.sealed.GenericState
import domain.repository.FinanceRepository
import model.MonthDetailScreenModel

class GetIncomeMonthDetailUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend operator fun invoke(params: Params): GenericState<MonthDetailScreenModel> =
        financeRepository.getIncomeMonthDetail(
            monthKey = params.monthKey
        )

    data class Params(
        val monthKey: String
    )
}
