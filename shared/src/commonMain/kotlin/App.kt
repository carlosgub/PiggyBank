import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.CompositionLocalProvider
import core.navigation.LocalNavController
import kotlinx.serialization.json.Json
import model.CategoryMonthDetailArgs
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import presentation.navigation.NavArgs
import presentation.navigation.Screen
import presentation.screen.categorymonthdetail.CategoryMonthDetailScreen
import presentation.screen.create.CreateScreen
import presentation.screen.edit.EditScreen
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
                    initialRoute = Screen.Home.createRoute(
                        getCurrentMonthKey()
                    )
                ) {
                    scene(route = Screen.Home.route) { backStackEntry ->
                        val monthKey: String = backStackEntry.path<String>(NavArgs.HomeArgs.key)!!
                        HomeScreen(
                            monthKey = monthKey
                        )
                    }
                    scene(route = Screen.CreateScreen.route) { backStackEntry ->
                        val financeName: String =
                            backStackEntry.path<String>(NavArgs.CreateArgs.key)!!
                        CreateScreen(
                            financeName = financeName
                        )
                    }
                    scene(route = Screen.EditScreen.route) { backStackEntry ->
                        val id = backStackEntry.path<Long>(NavArgs.EditId.key)!!
                        val financeName = backStackEntry.path<String>(NavArgs.EditFinanceName.key)!!
                        EditScreen(
                            id = id,
                            financeName = financeName
                        )
                    }
                    scene(route = Screen.MonthsScreen.route) {
                        MonthsScreen()
                    }
                    scene(route = Screen.CategoryMonthDetailScreen.route) { backStackEntry ->
                        val args: CategoryMonthDetailArgs =
                            Json.decodeFromString(backStackEntry.path<String>(NavArgs.CategoryMonthDetailArgs.key)!!)
                        CategoryMonthDetailScreen(
                            args = args
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = ColorPrimary,
            surface = Color.White
        ),
        shapes = Shapes,
        typography = Typography
    ) {
        content()
    }
}
