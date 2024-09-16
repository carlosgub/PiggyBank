package com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetailincome

import com.carlosgub.myfinances.domain.model.CategoryEnum
import com.carlosgub.myfinances.presentation.model.MonthDetailIncomeScreenModel

data class CategoryMonthDetailIncomeScreenState(
    val monthDetail: MonthDetailIncomeScreenModel = MonthDetailIncomeScreenModel(),
    val showLoading: Boolean = false,
    val monthKey: String = "",
    val category: CategoryEnum = CategoryEnum.FOOD,
    val isInitialDataLoaded: Boolean = false,
)
