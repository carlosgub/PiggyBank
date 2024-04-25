package presentation.viewmodel.monthDetail

import kotlinx.coroutines.Job
import model.ExpenseScreenModel

interface CategoryMonthDetailScreenIntents {
    fun setInitialConfiguration(
        monthKey: String,
        category: String
    ): Job

    fun getMonthDetail(): Job
    fun navigateToEditExpense(expenseScreenModel: ExpenseScreenModel): Job
    fun updateBackScreen(): Job
}
