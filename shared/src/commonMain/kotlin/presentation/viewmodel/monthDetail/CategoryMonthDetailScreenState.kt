package presentation.viewmodel.monthDetail

import model.CategoryEnum
import model.MonthDetailScreenModel

data class CategoryMonthDetailScreenState(
    val monthDetail: MonthDetailScreenModel = MonthDetailScreenModel(),
    val showLoading: Boolean = false,
    val monthKey: String = "",
    val category: CategoryEnum = CategoryEnum.FOOD,
    val isInitialDataLoaded: Boolean = false,
    val updateBackScreen: Boolean = false
)
