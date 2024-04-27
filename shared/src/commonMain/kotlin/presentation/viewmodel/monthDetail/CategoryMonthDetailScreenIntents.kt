package presentation.viewmodel.monthDetail

import kotlinx.coroutines.Job
import domain.model.ExpenseScreenModel

interface CategoryMonthDetailScreenIntents {
    fun setInitialConfiguration(
        monthKey: String,
        category: String
    ): Job

    fun getMonthDetail(): Job
    fun navigateToEditExpense(expenseScreenModel: ExpenseScreenModel): Job
}
