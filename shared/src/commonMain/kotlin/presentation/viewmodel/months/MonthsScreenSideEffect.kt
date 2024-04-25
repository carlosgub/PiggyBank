package presentation.viewmodel.months

sealed class MonthsScreenSideEffect {
    data class NavigateToMonthDetail(val monthKey: String) : MonthsScreenSideEffect()
}
