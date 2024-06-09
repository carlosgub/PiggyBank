package domain.model

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
import com.carlosgub.myfinances.theme.Brown
import com.carlosgub.myfinances.theme.ColorOrange
import com.carlosgub.myfinances.theme.ColorPrimary
import com.carlosgub.myfinances.theme.Pink
import com.carlosgub.myfinances.theme.Purple
import com.carlosgub.myfinances.theme.Teal
import myfinances.shared.generated.resources.Res
import myfinances.shared.generated.resources.category_clothes
import myfinances.shared.generated.resources.category_food
import myfinances.shared.generated.resources.category_home
import myfinances.shared.generated.resources.category_love
import myfinances.shared.generated.resources.category_personal
import myfinances.shared.generated.resources.category_pets
import myfinances.shared.generated.resources.category_savings
import myfinances.shared.generated.resources.category_study
import myfinances.shared.generated.resources.category_taxi
import myfinances.shared.generated.resources.category_work
import org.jetbrains.compose.resources.StringResource

enum class CategoryEnum(
    val categoryName: StringResource,
    val icon: ImageVector,
    val color: Color = ColorPrimary,
    val type: FinanceEnum,
) {
    FOOD(
        categoryName = Res.string.category_food,
        icon = Icons.Filled.Restaurant,
        color = Color.Red,
        type = FinanceEnum.EXPENSE,
    ),
    CLOTHES(
        categoryName = Res.string.category_clothes,
        icon = Icons.Filled.Checkroom,
        color = Pink,
        type = FinanceEnum.EXPENSE,
    ),
    HOME(
        categoryName = Res.string.category_home,
        icon = Icons.Filled.Home,
        color = ColorOrange,
        type = FinanceEnum.EXPENSE,
    ),
    PERSONAL(
        categoryName = Res.string.category_personal,
        icon = Icons.Filled.Man,
        color = Brown,
        type = FinanceEnum.EXPENSE,
    ),
    STUDY(
        categoryName = Res.string.category_study,
        icon = Icons.Filled.Book,
        color = ColorPrimary,
        type = FinanceEnum.EXPENSE,
    ),
    SAVINGS(
        categoryName = Res.string.category_savings,
        icon = Icons.Filled.Savings,
        color = Purple,
        type = FinanceEnum.EXPENSE,
    ),
    PETS(
        categoryName = Res.string.category_pets,
        icon = Icons.Filled.Pets,
        color = Teal,
        type = FinanceEnum.EXPENSE,
    ),
    TAXI(
        categoryName = Res.string.category_taxi,
        icon = Icons.Filled.LocalTaxi,
        color = Color.Yellow,
        type = FinanceEnum.EXPENSE,
    ),
    LOVE(
        categoryName = Res.string.category_love,
        icon = Icons.Filled.Favorite,
        color = Color.Red,
        type = FinanceEnum.EXPENSE,
    ),
    WORK(
        categoryName = Res.string.category_work,
        icon = Icons.Filled.Work,
        color = Color.Green,
        type = FinanceEnum.INCOME,
    ), ;

    companion object {
        fun getCategoryEnumFromName(name: String): CategoryEnum {
            for (enum in entries) {
                if (enum.name == name) {
                    return enum
                }
            }
            return entries.first()
        }
    }
}
