package domain.usecase

import core.sealed.GenericState
import domain.repository.FinanceRepository
import model.CategoryEnum
import model.MonthDetailScreenModel

class GetExpenseMonthDetailUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend fun getExpenseMonthDetail(params: Params): GenericState<MonthDetailScreenModel> =
        financeRepository.getExpenseMonthDetail(
            categoryEnum = params.categoryEnum,
            monthKey = params.monthKey
        )

    data class Params(
        val categoryEnum: CategoryEnum,
        val monthKey: String
    )
}
