package core.navigation

import androidx.compose.runtime.compositionLocalOf
import moe.tlaster.precompose.navigation.Navigator

val LocalNavController = compositionLocalOf<Navigator> { error("No NavController found!") }