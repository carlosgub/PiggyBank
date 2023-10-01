package presentation.navigation

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.CategoryMonthDetailArgs
import model.CreateArgs
import model.HomeArgs

sealed class Screen(val route: String) {
    object Home : Screen("Home/{${NavArgs.HomeArgs.key}}") {
        fun createRoute(homeArgs: HomeArgs) =
            "Home/${Json.encodeToString(homeArgs)}"
    }

    object CreateScreen : Screen("CreateScreen/{${NavArgs.CreateArgs.key}}") {
        fun createRoute(createArgs: CreateArgs) =
            "CreateScreen/${Json.encodeToString(createArgs)}"
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
    CreateArgs("CreateArgs")
}
