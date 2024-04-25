package presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("Home/{${NavArgs.MONTH_KEY.key}}") {
        fun createRoute(monthKey: String) =
            "Home/$monthKey"
    }

    object CreateScreen : Screen("CreateScreen/{${NavArgs.FINANCE_NAME.key}}") {
        fun createRoute(financeName: String) =
            "CreateScreen/$financeName"
    }

    object EditScreen :
        Screen("EditScreen/{${NavArgs.ID.key}}/{${NavArgs.FINANCE_NAME.key}}") {
        fun createRoute(id: Long, financeName: String) =
            "EditScreen/$id/$financeName"
    }

    object MonthsScreen : Screen("MonthsScreen")

    object CategoryMonthDetailScreen :
        Screen("CategoryMonthDetailScreen/{${NavArgs.MONTH_KEY.key}}/{${NavArgs.CATEGORY_NAME.key}}") {
        fun createRoute(monthKey: String, categoryName: String) =
            "CategoryMonthDetailScreen/$monthKey/$categoryName"
    }
}

enum class NavArgs(val key: String) {
    ID("id"),
    MONTH_KEY("monthKey"),
    FINANCE_NAME("financeName"),
    CATEGORY_NAME("categoryName")
}
