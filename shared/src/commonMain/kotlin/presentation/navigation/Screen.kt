package presentation.navigation

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.CategoryMonthDetailArgs

sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object CreateExpenseScreen : Screen("CreateExpenseScreen")
    object CreateIncomeScreen : Screen("CreateIncomeScreen")

    object CategoryMonthDetailScreen : Screen("CategoryMonthDetailScreen/{${NavArgs.CategoryMonthDetailArgs.key}}") {
        fun createRoute(categoryMonthDetailArgs: CategoryMonthDetailArgs) =
            "CategoryMonthDetailScreen/${Json.encodeToString(categoryMonthDetailArgs)}"
    }
}

enum class NavArgs(val key: String) {
    CategoryMonthDetailArgs("CategoryMonthDetailArgs")
}
