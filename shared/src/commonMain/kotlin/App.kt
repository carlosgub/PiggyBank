import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import di.dataModule
import di.homeModule
import kotlinx.serialization.json.Json
import model.CategoryMonthDetailArgs
import model.CreateArgs
import model.EditArgs
import model.HomeArgs
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import org.koin.compose.KoinApplication
import presentation.navigation.NavArgs
import presentation.navigation.Screen
import presentation.screen.CategoryMonthDetailScreen
import presentation.screen.CreateScreen
import presentation.screen.EditScreen
import presentation.screen.HomeScreen
import presentation.screen.MonthsScreen
import theme.ColorPrimary
import theme.Shapes
import theme.Typography
import utils.getCurrentMonthKey

@Composable
fun App() {
    val navigator = rememberNavigator()
    AppTheme {
        NavHost(
            navigator = navigator,
            initialRoute = Screen.Home.createRoute(
                HomeArgs(
                    monthKey = getCurrentMonthKey(),
                    isHome = true
                )
            )
        ) {
            scene(route = Screen.Home.route) { backStackEntry ->
                val args: HomeArgs =
                    Json.decodeFromString(backStackEntry.path<String>(NavArgs.HomeArgs.key)!!)
                HomeScreen(
                    navigator = navigator,
                    args = args
                )
            }
            scene(route = Screen.CreateScreen.route) { backStackEntry ->
                val args: CreateArgs =
                    Json.decodeFromString(backStackEntry.path<String>(NavArgs.CreateArgs.key)!!)
                CreateScreen(
                    navigator = navigator,
                    args = args
                )
            }
            scene(route = Screen.EditScreen.route) { backStackEntry ->
                val args: EditArgs =
                    Json.decodeFromString(backStackEntry.path<String>(NavArgs.EditArgs.key)!!)
                EditScreen(
                    navigator = navigator,
                    args = args
                )
            }
            scene(route = Screen.MonthsScreen.route) {
                MonthsScreen(
                    navigator = navigator
                )
            }
            scene(route = Screen.CategoryMonthDetailScreen.route) { backStackEntry ->
                val args: CategoryMonthDetailArgs =
                    Json.decodeFromString(backStackEntry.path<String>(NavArgs.CategoryMonthDetailArgs.key)!!)
                CategoryMonthDetailScreen(
                    navigator = navigator,
                    args = args
                )
            }
        }
    }
}

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    KoinApplication(
        application = {
            modules(homeModule, dataModule)
        }
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
}
