package model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Savings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import theme.ColorPrimary

enum class CategoryEnum(
    val categoryName: String,
    val icon: ImageVector,
    val color: Color = ColorPrimary
) {
    FOOD(
        categoryName = "Food",
        icon = Icons.Filled.Restaurant
    ),
    CLOTHES(
        categoryName = "Clothes",
        icon = Icons.Filled.Checkroom
    ),
    HOME(
        categoryName = "Home",
        icon = Icons.Filled.Home
    ),
    PERSONAL(
        categoryName = "Personal",
        icon = Icons.Filled.Man
    ),
    STUDY(
        categoryName = "Studies",
        icon = Icons.Filled.Book
    ),
    SAVINGS(
        categoryName = "Savings",
        icon = Icons.Filled.Savings
    )
}