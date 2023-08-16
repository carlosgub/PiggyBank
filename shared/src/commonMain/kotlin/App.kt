import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import di.homeModule
import org.koin.compose.KoinApplication
import view.screen.HomeScreen

@Composable
fun App() {
    AppTheme {
        Navigator(HomeScreen())
    }
}

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    KoinApplication(
        application = {
            modules(homeModule)
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