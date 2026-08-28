package com.carlosgub.myfinances.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.read
import com.carlosgub.myfinances.core.navigation.LocalNavController
import com.carlosgub.myfinances.core.utils.getCurrentMonthKey
import com.carlosgub.myfinances.presentation.screen.categorymonthdetailexpense.CategoryMonthDetailScreenExpense
import com.carlosgub.myfinances.presentation.screen.categorymonthdetailincome.CategoryMonthDetailScreenIncome
import com.carlosgub.myfinances.presentation.screen.createexpense.CreateExpenseScreen
import com.carlosgub.myfinances.presentation.screen.createincome.CreateIncomeScreen
import com.carlosgub.myfinances.presentation.screen.editexpense.EditExpenseScreen
import com.carlosgub.myfinances.presentation.screen.editincome.EditIncomeScreen
import com.carlosgub.myfinances.presentation.screen.home.HomeScreen
import com.carlosgub.myfinances.presentation.screen.month.MonthsScreen
import com.carlosgub.myfinances.theme.ColorPrimary
import com.carlosgub.myfinances.theme.Shapes
import com.carlosgub.myfinances.theme.Typography

@Composable
fun App() {
    val navController: NavHostController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navController) {
        MyFinancesTheme {
            NavHost(
                navController = navController,
                startDestination = Navigation.Home.createRoute(
                    getCurrentMonthKey(),
                ),
            ) {
                composable(route = Navigation.Home.route) { backStackEntry ->
                    val monthKey: String = backStackEntry.arguments?.read { getString(NavArgs.MONTH_KEY.key) }!!
                    HomeScreen(
                        monthKey = monthKey,
                    )
                }
                composable(route = Navigation.CreateExpenseScreen.route) {
                    CreateExpenseScreen()
                }
                composable(route = Navigation.CreateIncomeScreen.route) {
                    CreateIncomeScreen()
                }
                composable(
                    route = Navigation.EditExpenseScreen.route,
                    arguments = Navigation.EditExpenseScreen.getArguments(),
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.read { getLong(NavArgs.ID.key) }!!
                    EditExpenseScreen(
                        id = id,
                    )
                }
                composable(
                    route = Navigation.EditIncomeScreen.route,
                    arguments = Navigation.EditIncomeScreen.getArguments(),
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.read { getLong(NavArgs.ID.key) }!!
                    EditIncomeScreen(
                        id = id,
                    )
                }
                composable(route = Navigation.MonthsScreen.route) {
                    MonthsScreen()
                }
                composable(route = Navigation.CategoryMonthDetailExpenseScreen.route) { backStackEntry ->
                    val monthKey =
                        backStackEntry.arguments?.read { getString(NavArgs.MONTH_KEY.key) }!!
                    val categoryName =
                        backStackEntry.arguments?.read { getString(NavArgs.CATEGORY_NAME.key) }!!
                    CategoryMonthDetailScreenExpense(
                        monthKey = monthKey,
                        categoryName = categoryName,
                    )
                }
                composable(route = Navigation.CategoryMonthDetailIncomeScreen.route) { backStackEntry ->
                    val monthKey =
                        backStackEntry.arguments?.read { getString(NavArgs.MONTH_KEY.key) }!!
                    val categoryName =
                        backStackEntry.arguments?.read { getString(NavArgs.CATEGORY_NAME.key) }!!
                    CategoryMonthDetailScreenIncome(
                        monthKey = monthKey,
                        categoryName = categoryName,
                    )
                }
            }
        }
    }
}

@Composable
fun MyFinancesTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = ColorPrimary,
            surface = Color.White,
        ),
        shapes = Shapes,
        typography = Typography,
    ) {
        content()
    }
}
