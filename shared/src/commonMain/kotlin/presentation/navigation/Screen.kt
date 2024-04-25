package presentation.navigation

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.CategoryMonthDetailArgs

sealed class Screen(val route: String) {
    object Home : Screen("Home/{${NavArgs.HomeArgs.key}}") {
        fun createRoute(monthKey: String) =
            "Home/$monthKey"
    }

    object CreateScreen : Screen("CreateScreen/{${NavArgs.CreateArgs.key}}") {
        fun createRoute(financeName: String) =
            "CreateScreen/$financeName"
    }

    object EditScreen :
        Screen("EditScreen/{${NavArgs.EditId.key}}/{${NavArgs.EditFinanceName.key}}") {
        fun createRoute(id: Long, financeName: String) =
            "EditScreen/$id/$financeName"
    }

    object MonthsScreen : Screen("MonthsScreen")

    object CategoryMonthDetailScreen :
        Screen("CategoryMonthDetailScreen/{${NavArgs.CategoryMonthDetailArgs.key}}") {
        fun createRoute(categoryMonthDetailArgs: CategoryMonthDetailArgs) =
            "CategoryMonthDetailScreen/${Json.encodeToString(categoryMonthDetailArgs)}"
    }
}

enum class NavArgs(val key: String) {
    CategoryMonthDetailArgs("CategoryMonthDetailArgs"),
    HomeArgs("HomeArgs"),
    CreateArgs("CreateArgs"),
    EditId("EditId"),
    EditFinanceName("EditFinanceName")
}
