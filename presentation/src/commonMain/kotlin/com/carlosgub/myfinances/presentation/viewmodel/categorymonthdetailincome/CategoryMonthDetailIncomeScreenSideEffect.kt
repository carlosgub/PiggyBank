package com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetailincome

import com.carlosgub.myfinances.presentation.model.IncomeScreenModel

sealed class CategoryMonthDetailIncomeScreenSideEffect {
    data class NavigateToMonthDetail(
        val incomeScreenModel: IncomeScreenModel,
    ) : CategoryMonthDetailIncomeScreenSideEffect()
}
