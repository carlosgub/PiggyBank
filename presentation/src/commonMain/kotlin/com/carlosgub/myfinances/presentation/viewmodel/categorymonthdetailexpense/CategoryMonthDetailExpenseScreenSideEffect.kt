package com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetailexpense

import com.carlosgub.myfinances.presentation.model.ExpenseScreenModel

sealed class CategoryMonthDetailExpenseScreenSideEffect {
    data class NavigateToMonthDetail(
        val expenseScreenModel: ExpenseScreenModel,
    ) : CategoryMonthDetailExpenseScreenSideEffect()
}
