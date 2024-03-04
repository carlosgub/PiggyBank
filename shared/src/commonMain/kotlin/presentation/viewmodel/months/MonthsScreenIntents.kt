package presentation.viewmodel.months

import kotlinx.coroutines.Job

interface MonthsScreenIntents {
    fun getMonths(): Job
}
