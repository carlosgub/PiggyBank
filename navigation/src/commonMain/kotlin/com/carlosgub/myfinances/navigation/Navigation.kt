package com.carlosgub.myfinances.navigation

sealed class Navigation(val route: String) {
    data object Home : Navigation("Home/{${NavArgs.MONTH_KEY.key}}") {
        fun createRoute(monthKey: String) = "Home/$monthKey"
    }

    data object CreateIncomeScreen : Navigation("CreateIncomeScreen")

    data object CreateExpenseScreen : Navigation("CreateExpenseScreen")

    data object EditExpenseScreen :
        Navigation("EditExpenseScreen/{${NavArgs.ID.key}}") {
        fun createRoute(id: Long) = "EditExpenseScreen/$id"
    }

    data object EditIncomeScreen :
        Navigation("EditIncomeScreen/{${NavArgs.ID.key}}") {
        fun createRoute(id: Long) = "EditIncomeScreen/$id"
    }

    data object MonthsScreen : Navigation("MonthsScreen")

    data object CategoryMonthDetailScreen :
        Navigation("CategoryMonthDetailScreen/{${NavArgs.MONTH_KEY.key}}/{${NavArgs.CATEGORY_NAME.key}}") {
        fun createRoute(
            monthKey: String,
            categoryName: String,
        ) = "CategoryMonthDetailScreen/$monthKey/$categoryName"
    }
}

enum class NavArgs(val key: String) {
    ID("id"),
    MONTH_KEY("monthKey"),
    CATEGORY_NAME("categoryName"),
}
