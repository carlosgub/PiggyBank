package presentation.viewmodel.monthDetail

import model.ExpenseScreenModel

sealed class CategoryMonthDetailScreenSideEffect {
    data class NavigateToMonthDetail(
        val expenseScreenModel: ExpenseScreenModel
    ) : CategoryMonthDetailScreenSideEffect()
}
