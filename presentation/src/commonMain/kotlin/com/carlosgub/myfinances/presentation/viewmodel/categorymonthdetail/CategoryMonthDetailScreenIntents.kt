package com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetail

import com.carlosgub.myfinances.presentation.model.ExpenseScreenModel
import kotlinx.coroutines.Job

interface CategoryMonthDetailScreenIntents {
    fun setInitialConfiguration(
        monthKey: String,
        category: String,
    ): Job

    fun getMonthDetail(): Job

    fun navigateToEditExpense(expenseScreenModel: ExpenseScreenModel): Job
}
