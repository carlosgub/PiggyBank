package presentation.viewmodel.months

import kotlinx.datetime.LocalDateTime
import model.HomeArgs

sealed class MonthsScreenSideEffect {
    data class NavigateToMonthDetail(val homeArgs: HomeArgs) : MonthsScreenSideEffect()
}