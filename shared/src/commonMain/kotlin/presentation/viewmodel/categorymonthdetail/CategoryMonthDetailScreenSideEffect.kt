package presentation.viewmodel.categorymonthdetail

import domain.model.ExpenseScreenModel

sealed class CategoryMonthDetailScreenSideEffect {
    data class NavigateToMonthDetail(
        val expenseScreenModel: ExpenseScreenModel,
    ) : CategoryMonthDetailScreenSideEffect()
}
