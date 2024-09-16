package com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetailexpense

import com.carlosgub.myfinances.domain.model.CategoryEnum
import com.carlosgub.myfinances.presentation.model.MonthDetailExpenseScreenModel

data class CategoryMonthDetailExpenseScreenState(
    val monthDetail: MonthDetailExpenseScreenModel = MonthDetailExpenseScreenModel(),
    val showLoading: Boolean = false,
    val monthKey: String = "",
    val category: CategoryEnum = CategoryEnum.FOOD,
    val isInitialDataLoaded: Boolean = false,
)
