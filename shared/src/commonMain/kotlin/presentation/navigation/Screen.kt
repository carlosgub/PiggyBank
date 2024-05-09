package presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("Home/{${NavArgs.MONTH_KEY.key}}") {
        fun createRoute(monthKey: String) = "Home/$monthKey"
    }

    object CreateIncomeScreen : Screen("CreateIncomeScreen")

    object CreateExpenseScreen : Screen("CreateExpenseScreen")

    object EditExpenseScreen :
        Screen("EditExpenseScreen/{${NavArgs.ID.key}}") {
        fun createRoute(id: Long) = "EditExpenseScreen/$id"
    }

    data object EditIncomeScreen :
        Screen("EditIncomeScreen/{${NavArgs.ID.key}}") {
        fun createRoute(id: Long) = "EditIncomeScreen/$id"
    }

    object MonthsScreen : Screen("MonthsScreen")

    object CategoryMonthDetailScreen :
        Screen("CategoryMonthDetailScreen/{${NavArgs.MONTH_KEY.key}}/{${NavArgs.CATEGORY_NAME.key}}") {
        fun createRoute(
            monthKey: String,
            categoryName: String
        ) = "CategoryMonthDetailScreen/$monthKey/$categoryName"
    }
}

enum class NavArgs(val key: String) {
    ID("id"),
    MONTH_KEY("monthKey"),
    CATEGORY_NAME("categoryName")
}
