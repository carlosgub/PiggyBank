@file:OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)

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
import model.CategoryEnum
import model.FinanceEnum
import theme.ColorPrimary
import theme.Gray100
import theme.Gray400
import theme.Gray600

@Composable
fun CategoriesChips(
    selectedSelected: CategoryEnum,
    onChipPressed: (CategoryEnum) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        val categoriesList = CategoryEnum.entries.filter { it.type == FinanceEnum.EXPENSE }
        Text(
            "Categories",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 16.dp)
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categoriesList.forEach { categoryEnum ->
                CategoryChip(
                    categoryEnum,
                    selectedSelected,
                    onChipPressed = onChipPressed
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(
    categoryEnum: CategoryEnum,
    selected: CategoryEnum,
    onChipPressed: (CategoryEnum) -> Unit
) {
    val chipBackgroundColor = if (selected == categoryEnum) {
        ColorPrimary.copy(alpha = 0.2f)
    } else {
        Gray100
    }
    val contentColor = if (selected == categoryEnum) {
        ColorPrimary
    } else {
        Gray600
    }
    val chipBorderStroke = if (selected == categoryEnum) {
        BorderStroke(
            width = 1.dp,
            color = ColorPrimary
        )
    } else {
        BorderStroke(
            width = 1.dp,
            color = Gray400
        )
    }
    Chip(
        onClick = {
            onChipPressed(categoryEnum)
        },
        colors = ChipDefaults.chipColors(
            backgroundColor = chipBackgroundColor
        ),
        shape = RoundedCornerShape(12.dp),
        border = chipBorderStroke
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                horizontal = 6.dp,
                vertical = 4.dp
            )
        ) {
            Icon(
                imageVector = categoryEnum.icon,
                contentDescription = null,
                tint = contentColor
            )
            Text(
                categoryEnum.categoryName,
                color = contentColor,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}