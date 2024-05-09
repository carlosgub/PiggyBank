package domain.usecase

import core.sealed.GenericState
import domain.model.CategoryEnum
import domain.model.MonthDetailScreenModel
import domain.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow

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
