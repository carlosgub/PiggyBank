package presentation.viewmodel.monthDetail

import domain.model.ExpenseScreenModel

sealed class CategoryMonthDetailScreenSideEffect {
    data class NavigateToMonthDetail(
        val expenseScreenModel: ExpenseScreenModel,
    ) : CategoryMonthDetailScreenSideEffect()
}
