package presentation.viewmodel.monthDetail

import kotlinx.coroutines.Job
import model.CategoryMonthDetailArgs
import model.ExpenseScreenModel

interface CategoryMonthDetailScreenIntents {
    fun setInitialConfiguration(args: CategoryMonthDetailArgs): Job
    fun getMonthDetail(): Job
    fun navigateToEditExpense(expenseScreenModel: ExpenseScreenModel): Job
    fun updateBackScreen():Job
}
