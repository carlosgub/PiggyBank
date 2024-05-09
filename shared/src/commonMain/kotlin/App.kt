import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import core.navigation.LocalNavController
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import presentation.navigation.NavArgs
import presentation.navigation.Screen
import presentation.screen.categorymonthdetail.CategoryMonthDetailScreen
import presentation.screen.createexpense.CreateExpenseScreen
import presentation.screen.createincome.CreateIncomeScreen
import presentation.screen.editexpense.EditExpenseScreen
import presentation.screen.editincome.EditIncomeScreen
import presentation.screen.home.HomeScreen
import presentation.screen.month.MonthsScreen
import theme.ColorPrimary
import theme.Shapes
import theme.Typography
import utils.getCurrentMonthKey

@Composable
fun App() {
    PreComposeApp {
        val navigator = rememberNavigator()
        CompositionLocalProvider(LocalNavController provides navigator) {
            AppTheme {
                NavHost(
                    navigator = navigator,
                    initialRoute =
                        Screen.Home.createRoute(
                            getCurrentMonthKey(),
                        ),
                ) {
                    scene(route = Screen.Home.route) { backStackEntry ->
                        val monthKey: String = backStackEntry.path<String>(NavArgs.MONTH_KEY.key)!!
                        HomeScreen(
                            monthKey = monthKey,
                        )
                    }
                    scene(route = Screen.CreateExpenseScreen.route) {
                        CreateExpenseScreen()
                    }
                    scene(route = Screen.CreateIncomeScreen.route) {
                        CreateIncomeScreen()
                    }
                    scene(route = Screen.EditExpenseScreen.route) { backStackEntry ->
                        val id = backStackEntry.path<Long>(NavArgs.ID.key)!!
                        EditExpenseScreen(
                            id = id,
                        )
                    }
                    scene(route = Screen.EditIncomeScreen.route) { backStackEntry ->
                        val id = backStackEntry.path<Long>(NavArgs.ID.key)!!
                        EditIncomeScreen(
                            id = id,
                        )
                    }
                    scene(route = Screen.MonthsScreen.route) {
                        MonthsScreen()
                    }
                    scene(route = Screen.CategoryMonthDetailScreen.route) { backStackEntry ->
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

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme =
            MaterialTheme.colorScheme.copy(
                primary = ColorPrimary,
                surface = Color.White,
            ),
        shapes = Shapes,
        typography = Typography,
    ) {
        content()
    }
}
