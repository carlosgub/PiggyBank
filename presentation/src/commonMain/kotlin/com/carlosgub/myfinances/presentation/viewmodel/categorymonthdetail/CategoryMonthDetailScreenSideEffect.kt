package com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetail

import com.carlosgub.myfinances.presentation.model.ExpenseScreenModel

sealed class CategoryMonthDetailScreenSideEffect {
    data class NavigateToMonthDetail(
        val expenseScreenModel: ExpenseScreenModel,
    ) : CategoryMonthDetailScreenSideEffect()
}
