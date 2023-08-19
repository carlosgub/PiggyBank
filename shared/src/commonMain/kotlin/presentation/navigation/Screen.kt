package presentation.navigation

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import model.Finance

sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object CreateExpenseScreen : Screen("CreateExpenseScreen")
    object CreateIncomeScreen : Screen("CreateIncomeScreen")
}