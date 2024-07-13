package com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetail

import com.carlosgub.myfinances.domain.model.ExpenseScreenModel

sealed class CategoryMonthDetailScreenSideEffect {
    data class NavigateToMonthDetail(
        val expenseScreenModel: ExpenseScreenModel,
    ) : CategoryMonthDetailScreenSideEffect()
}
