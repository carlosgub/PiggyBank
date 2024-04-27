package presentation.viewmodel.monthDetail

import domain.model.CategoryEnum
import domain.model.MonthDetailScreenModel

data class CategoryMonthDetailScreenState(
    val monthDetail: MonthDetailScreenModel = MonthDetailScreenModel(),
    val showLoading: Boolean = false,
    val monthKey: String = "",
    val category: CategoryEnum = CategoryEnum.FOOD,
    val isInitialDataLoaded: Boolean = false
)
