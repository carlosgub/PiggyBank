package domain.usecase

import core.sealed.GenericState
import domain.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow
import model.CategoryEnum
import model.MonthDetailScreenModel

class GetExpenseMonthDetailUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend operator fun invoke(params: Params): Flow<GenericState<MonthDetailScreenModel>> =
        financeRepository.getExpenseMonthDetail(
            categoryEnum = params.categoryEnum,
            monthKey = params.monthKey
        )

    data class Params(
        val categoryEnum: CategoryEnum,
        val monthKey: String
    )
}
