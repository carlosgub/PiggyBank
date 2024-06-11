package presentation.navigation

import moe.tlaster.precompose.navigation.Navigator

interface AppNavigation {
    fun navigateToEditIncome(
        navigator: Navigator,
        id: Long,
    )

    fun navigateToHome(
        navigator: Navigator,
        monthKey: String,
    )

    fun navigateToEditExpense(
        navigator: Navigator,
        id: Long,
    )

    fun navigateToMonthDetail(
        navigator: Navigator,
        monthKey: String,
        categoryName: String,
    )

    fun navigateToMonths(navigator: Navigator)

    fun navigateToAddExpense(navigator: Navigator)

    fun navigateToAddIncome(navigator: Navigator)
}
