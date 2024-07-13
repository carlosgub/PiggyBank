package com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetail

import com.carlosgub.myfinances.domain.model.CategoryEnum
import com.carlosgub.myfinances.domain.model.MonthDetailScreenModel

data class CategoryMonthDetailScreenState(
    val monthDetail: MonthDetailScreenModel = MonthDetailScreenModel(),
    val showLoading: Boolean = false,
    val monthKey: String = "",
    val category: CategoryEnum = CategoryEnum.FOOD,
    val isInitialDataLoaded: Boolean = false,
)
