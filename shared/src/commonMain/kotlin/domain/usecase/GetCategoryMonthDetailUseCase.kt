package domain.usecase

import core.sealed.GenericState
import domain.repository.FinanceRepository
import model.CategoryEnum
import model.Expense
import model.FinanceScreenModel

class GetCategoryMonthDetailUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend fun getCategoryMonthDetail(params: Params): GenericState<List<Expense>> =
        financeRepository.getCategoryMonthDetail(
            categoryEnum = params.categoryEnum,
            monthKey = params.monthKey
        )

    data class Params(
        val categoryEnum: CategoryEnum,
        val monthKey: String
    )
}
