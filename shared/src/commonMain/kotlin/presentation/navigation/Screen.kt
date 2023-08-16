package presentation.navigation

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import model.Finance

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ImageDetailScreen : Screen("imageDetailScreen/{${NavArgs.BirdImage.key}}") {
        fun createRoute(finance: Finance) =
            "imageDetailScreen/${Json.encodeToString(finance)}"
    }
}

enum class NavArgs(val key: String) {
    BirdImage("Finance"),
}