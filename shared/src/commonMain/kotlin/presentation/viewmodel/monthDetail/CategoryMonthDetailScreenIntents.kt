package presentation.viewmodel.monthDetail

import kotlinx.coroutines.Job
import model.CategoryMonthDetailArgs

interface CategoryMonthDetailScreenIntents {
    fun setInitialConfiguration(args: CategoryMonthDetailArgs): Job
    fun getMonthDetail(): Job
}
