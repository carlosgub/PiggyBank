package presentation.viewmodel.home

import kotlinx.coroutines.Job

interface HomeScreenIntents {
    fun getFinanceStatus(): Job
    fun setMonthKey(monthKey: String): Job
}
