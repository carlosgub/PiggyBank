package presentation.viewmodel.months

import kotlinx.coroutines.Job
import model.HomeArgs

interface MonthsScreenIntents {
    fun getMonths(): Job
    fun navigateToMonthDetail(homeArgs: HomeArgs): Job
}
