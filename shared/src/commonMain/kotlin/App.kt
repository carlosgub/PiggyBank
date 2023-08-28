import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import di.dataModule
import di.homeModule
import kotlinx.serialization.json.Json
import model.CategoryMonthDetailArgs
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import org.koin.compose.KoinApplication
import presentation.navigation.NavArgs
import presentation.navigation.Screen
import presentation.screen.CategoryMonthDetailScreen
import presentation.screen.CreateExpenseScreen
import presentation.screen.CreateIncomeScreen
import presentation.screen.HomeScreen
import theme.Shapes
import theme.Typography

@Composable
fun App() {
    val navigator = rememberNavigator()
    AppTheme {
        NavHost(
            navigator = navigator,
            initialRoute = Screen.Home.route
        ) {
            scene(route = Screen.Home.route) {
                HomeScreen(navigator = navigator)
            }
            scene(route = Screen.CreateExpenseScreen.route) { backStackEntry ->
                CreateExpenseScreen(
                    navigator = navigator
                )
            }
            scene(route = Screen.CreateIncomeScreen.route) { backStackEntry ->
                CreateIncomeScreen(
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
            colors = MaterialTheme.colors.copy(primary = Color.Black),
            shapes = Shapes,
            typography = Typography
        ) {
            content()
        }
    }
}
