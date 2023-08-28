package model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Work
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
    val color: Color = ColorPrimary,
    val type: FinanceEnum
) {
    FOOD(
        categoryName = "Food",
        icon = Icons.Filled.Restaurant,
        color = Color.Red,
        type = FinanceEnum.EXPENSE
    ),
    CLOTHES(
        categoryName = "Clothes",
        icon = Icons.Filled.Checkroom,
        color = Pink,
        type = FinanceEnum.EXPENSE
    ),
    HOME(
        categoryName = "Home",
        icon = Icons.Filled.Home,
        color = ColorOrange,
        type = FinanceEnum.EXPENSE
    ),
    PERSONAL(
        categoryName = "Personal",
        icon = Icons.Filled.Man,
        color = Brown,
        type = FinanceEnum.EXPENSE
    ),
    STUDY(
        categoryName = "Studies",
        icon = Icons.Filled.Book,
        color = ColorPrimary,
        type = FinanceEnum.EXPENSE
    ),
    SAVINGS(
        categoryName = "Savings",
        icon = Icons.Filled.Savings,
        color = Purple,
        type = FinanceEnum.EXPENSE
    ),
    PETS(
        categoryName = "Pets",
        icon = Icons.Filled.Pets,
        color = Teal,
        type = FinanceEnum.EXPENSE
    ),
    TAXI(
        categoryName = "Taxi",
        icon = Icons.Filled.LocalTaxi,
        color = Color.Yellow,
        type = FinanceEnum.EXPENSE
    ),
    LOVE(
        categoryName = "Love",
        icon = Icons.Filled.Favorite,
        color = Color.Red,
        type = FinanceEnum.EXPENSE
    ),
    WORK(
        categoryName = "Work",
        icon = Icons.Filled.Work,
        color = Color.Green,
        type = FinanceEnum.INCOME
    )
}
