package presentation.viewmodel.home

sealed class HomeScreenSideEffect {
    data object NavigateToMonths : HomeScreenSideEffect()

    data class NavigateToMonthDetail(
        val categoryName: String,
    ) : HomeScreenSideEffect()

    data object NavigateToAddExpense : HomeScreenSideEffect()

    data object NavigateToAddIncome : HomeScreenSideEffect()
}
