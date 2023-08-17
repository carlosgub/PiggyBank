import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import di.dataModule
import di.homeModule
import kotlinx.serialization.json.Json
import model.Finance
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.koin.compose.KoinApplication
import presentation.navigation.NavArgs
import presentation.navigation.Screen
import presentation.screen.CreateExpenseScreen
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
                val finance: Finance =
                    Json.decodeFromString(backStackEntry.path<String>(NavArgs.Finance.key)!!)
                CreateExpenseScreen(
                    navigator = navigator,
                    finance = finance
                )
            }
            /*scene(
                route = Screen.ImageDetailScreen.route,
                navTransition = NavTransition()
            ) { backStackEntry ->
                *//*val finance: Finance =
                    Json.decodeFromString(backStackEntry.path<String>(NavArgs.BirdImage.key)!!)*//*
                CreateExpenseScreen(navigator)
            }*/
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