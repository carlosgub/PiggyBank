package com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetailexpense

import com.carlosgub.myfinances.presentation.model.ExpenseScreenModel
import kotlinx.coroutines.Job

interface CategoryMonthDetailExpenseScreenIntents {
    fun setInitialConfiguration(
        monthKey: String,
        category: String,
    ): Job

    fun getMonthDetail(): Job

    fun navigateToEditExpense(expenseScreenModel: ExpenseScreenModel): Job
}
