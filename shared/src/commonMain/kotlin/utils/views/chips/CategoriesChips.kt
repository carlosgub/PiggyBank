@file:OptIn(
    ExperimentalMaterialApi::class, ExperimentalLayoutApi::class,
    ExperimentalResourceApi::class
)

package utils.views.chips

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.model.CategoryEnum
import domain.model.FinanceEnum
import myapplication.shared.generated.resources.Res
import myapplication.shared.generated.resources.categories_header
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import theme.ColorPrimary
import theme.Gray100
import theme.Gray400
import theme.Gray600
import theme.spacing_2
import theme.spacing_4

@Composable
fun CategoriesChips(
    selectedSelected: CategoryEnum,
    onChipPressed: (CategoryEnum) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        val categoriesList = CategoryEnum.entries.filter { it.type == FinanceEnum.EXPENSE }
        Text(
            text = stringResource(Res.string.categories_header),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = spacing_4),
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing_2),
        ) {
            categoriesList.forEach { categoryEnum ->
                CategoryChip(
                    categoryEnum,
                    selectedSelected,
                    onChipPressed = onChipPressed,
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(
    categoryEnum: CategoryEnum,
    selected: CategoryEnum,
    onChipPressed: (CategoryEnum) -> Unit,
) {
    val chipBackgroundColor =
        if (selected == categoryEnum) {
            ColorPrimary.copy(alpha = 0.2f)
        } else {
            Gray100
        }
    val contentColor =
        if (selected == categoryEnum) {
            ColorPrimary
        } else {
            Gray600
        }
    val chipBorderStroke =
        if (selected == categoryEnum) {
            BorderStroke(
                width = 1.dp,
                color = ColorPrimary,
            )
        } else {
            BorderStroke(
                width = 1.dp,
                color = Gray400,
            )
        }
    Chip(
        onClick = {
            onChipPressed(categoryEnum)
        },
        colors =
        ChipDefaults.chipColors(
            backgroundColor = chipBackgroundColor,
        ),
        shape = RoundedCornerShape(12.dp),
        border = chipBorderStroke,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
            Modifier.padding(
                horizontal = 6.dp,
                vertical = 4.dp,
            ),
        ) {
            Icon(
                imageVector = categoryEnum.icon,
                contentDescription = null,
                tint = contentColor,
            )
            Text(
                text = stringResource(categoryEnum.categoryName),
                color = contentColor,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(start = 4.dp),
            )
        }
    }
}
