package presentation.viewmodel.home

import kotlinx.coroutines.Job
import domain.model.FinanceScreenExpenses

interface HomeScreenIntents {
    fun getFinanceStatus(): Job
    fun setMonthKey(monthKey: String): Job
    fun navigateToMonths(): Job
    fun navigateToMonthDetail(financeScreenExpenses: FinanceScreenExpenses): Job
    fun navigateToAddExpense(): Job
    fun navigateToAddIncome(): Job
}
