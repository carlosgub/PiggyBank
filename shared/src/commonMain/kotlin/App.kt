import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import di.dataModule
import di.homeModule
import kotlinx.serialization.json.Json
import model.BirdImage
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.koin.compose.KoinApplication
import presentation.navigation.NavArgs
import presentation.navigation.Screen
import presentation.screen.HomeScreen
import presentation.screen.ImageDetailScreen

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
            scene(
                route = Screen.ImageDetailScreen.route,
                navTransition = NavTransition()
            ) { backStackEntry ->
                val birdImage: BirdImage = Json.decodeFromString(backStackEntry.path<String>(NavArgs.BirdImage.key)!!)
                ImageDetailScreen(navigator, birdImage)
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
            shapes = MaterialTheme.shapes.copy(
                small = AbsoluteCutCornerShape(0.dp),
                medium = AbsoluteCutCornerShape(0.dp),
                large = AbsoluteCutCornerShape(0.dp)
            )
        ) {
            content()
        }
    }
}