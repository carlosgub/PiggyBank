import androidx.compose.ui.window.ComposeUIViewController
import view.screen.HomeScreen

fun MainViewController() = ComposeUIViewController { HomeScreen().Content() }