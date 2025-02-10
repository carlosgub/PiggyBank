package com.carlosgub.myfinances.presentation.viewmodel.home

import com.carlosgub.myfinances.domain.model.CategoryEnum
import kotlinx.coroutines.Job

interface HomeScreenIntents {
    fun getFinanceStatus(): Job

    fun setMonthKey(monthKey: String): Job

    fun navigateToMonths(): Job

    fun navigateToMonthDetail(category: CategoryEnum): Job

    fun navigateToAddExpense(): Job

    fun navigateToAddIncome(): Job
}
