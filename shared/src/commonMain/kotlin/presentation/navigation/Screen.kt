package presentation.navigation

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import model.BirdImage

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ImageDetailScreen : Screen("imageDetailScreen/{${NavArgs.BirdImage.key}}") {
        fun createRoute(birdImage: BirdImage) =
            "imageDetailScreen/${Json.encodeToString(birdImage)}"
    }
}

enum class NavArgs(val key: String) {
    BirdImage("BirdImage"),
}