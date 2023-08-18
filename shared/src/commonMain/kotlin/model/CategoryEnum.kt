package model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Savings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import theme.Brown
import theme.ColorOrange
import theme.ColorPrimary
import theme.Pink
import theme.Purple
import theme.Teal

enum class CategoryEnum(
    val categoryName: String,
    val icon: ImageVector,
    val color: Color = ColorPrimary
) {
    FOOD(
        categoryName = "Food",
        icon = Icons.Filled.Restaurant,
        color = Color.Red
    ),
    CLOTHES(
        categoryName = "Clothes",
        icon = Icons.Filled.Checkroom,
        color = Pink
    ),
    HOME(
        categoryName = "Home",
        icon = Icons.Filled.Home,
        color = ColorOrange
    ),
    PERSONAL(
        categoryName = "Personal",
        icon = Icons.Filled.Man,
        color = Brown
    ),
    STUDY(
        categoryName = "Studies",
        icon = Icons.Filled.Book,
        color = ColorPrimary
    ),
    SAVINGS(
        categoryName = "Savings",
        icon = Icons.Filled.Savings,
        color = Purple
    ),
    PETS(
        categoryName = "Pets",
        icon = Icons.Filled.Pets,
        color = Teal
    )
}