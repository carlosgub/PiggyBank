package com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetailincome

import com.carlosgub.myfinances.presentation.model.IncomeScreenModel
import kotlinx.coroutines.Job

interface CategoryMonthDetailIncomeScreenIntents {
    fun setInitialConfiguration(
        monthKey: String,
        category: String,
    ): Job

    fun getMonthDetail(): Job

    fun navigateToEditIncome(incomeScreenModel: IncomeScreenModel): Job
}
