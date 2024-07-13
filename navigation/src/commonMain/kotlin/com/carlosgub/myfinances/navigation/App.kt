package com.carlosgub.myfinances.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.carlosgub.myfinances.core.navigation.LocalNavController
import com.carlosgub.myfinances.core.utils.getCurrentMonthKey
import com.carlosgub.myfinances.presentation.screen.categorymonthdetail.CategoryMonthDetailScreen
import com.carlosgub.myfinances.presentation.screen.createexpense.CreateExpenseScreen
import com.carlosgub.myfinances.presentation.screen.createincome.CreateIncomeScreen
import com.carlosgub.myfinances.presentation.screen.editexpense.EditExpenseScreen
import com.carlosgub.myfinances.presentation.screen.editincome.EditIncomeScreen
import com.carlosgub.myfinances.presentation.screen.home.HomeScreen
import com.carlosgub.myfinances.presentation.screen.month.MonthsScreen
import com.carlosgub.myfinances.theme.ColorPrimary
import com.carlosgub.myfinances.theme.Shapes
import com.carlosgub.myfinances.theme.Typography
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import org.koin.compose.KoinContext

@Composable
fun App() {
    KoinContext {
        PreComposeApp {
            val navigator = rememberNavigator()
            CompositionLocalProvider(LocalNavController provides navigator) {
                MyFinancesTheme {
                    NavHost(
                        navigator = navigator,
                        initialRoute = Navigation.Home.createRoute(
                            getCurrentMonthKey(),
                        ),
                    ) {
                        scene(route = Navigation.Home.route) { backStackEntry ->
                            val monthKey: String = backStackEntry.path<String>(NavArgs.MONTH_KEY.key)!!
                            HomeScreen(
                                monthKey = monthKey,
                            )
                        }
                        scene(route = Navigation.CreateExpenseScreen.route) {
                            CreateExpenseScreen()
                        }
                        scene(route = Navigation.CreateIncomeScreen.route) {
                            CreateIncomeScreen()
                        }
                        scene(route = Navigation.EditExpenseScreen.route) { backStackEntry ->
                            val id = backStackEntry.path<Long>(NavArgs.ID.key)!!
                            EditExpenseScreen(
                                id = id,
                            )
                        }
                        scene(route = Navigation.EditIncomeScreen.route) { backStackEntry ->
                            val id = backStackEntry.path<Long>(NavArgs.ID.key)!!
                            EditIncomeScreen(
                                id = id,
                            )
                        }
                        scene(route = Navigation.MonthsScreen.route) {
                            MonthsScreen()
                        }
                        scene(route = Navigation.CategoryMonthDetailScreen.route) { backStackEntry ->
                            val monthKey =
                                backStackEntry.path<String>(NavArgs.MONTH_KEY.key)!!
                            val categoryName =
                                backStackEntry.path<String>(NavArgs.CATEGORY_NAME.key)!!
                            CategoryMonthDetailScreen(
                                monthKey = monthKey,
                                categoryName = categoryName,
                            )
                        }
                    }
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
