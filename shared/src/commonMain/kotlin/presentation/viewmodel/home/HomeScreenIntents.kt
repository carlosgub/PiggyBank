package presentation.viewmodel.home

import domain.model.FinanceScreenExpenses
import kotlinx.coroutines.Job

interface HomeScreenIntents {
    fun getFinanceStatus(): Job

    fun setMonthKey(monthKey: String): Job

    fun navigateToMonths(): Job

    fun navigateToMonthDetail(categoryName: String): Job

    fun navigateToAddExpense(): Job

    fun navigateToAddIncome(): Job
}
