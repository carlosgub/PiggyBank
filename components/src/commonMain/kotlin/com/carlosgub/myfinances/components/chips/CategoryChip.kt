@file:OptIn(
    ExperimentalMaterialApi::class,
)

package com.carlosgub.myfinances.components.chips

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.carlosgub.myfinances.theme.ColorPrimary
import com.carlosgub.myfinances.theme.Gray100
import com.carlosgub.myfinances.theme.Gray600
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CategoryChip(
    text: StringResource,
    icon: ImageVector,
    selected: Boolean,
    onChipPressed: (StringResource) -> Unit,
) {
    val chipBackgroundColor = if (selected) {
        ColorPrimary.copy(alpha = 0.2f)
    } else {
        Gray100
    }
    val contentColor = if (selected) {
        ColorPrimary
    } else {
        Gray600
    }
    val chipBorderStroke = if (selected) {
        BorderStroke(
            width = 1.dp,
            color = ColorPrimary,
        )
    } else {
        BorderStroke(
            width = 1.dp,
            color = Gray600,
        )
    }
    val categoryText = stringResource(text)
    Chip(
        onClick = {
            onChipPressed(text)
        },
        colors = ChipDefaults.chipColors(
            backgroundColor = chipBackgroundColor,
        ),
        shape = RoundedCornerShape(12.dp),
        border = chipBorderStroke,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(
                    horizontal = 6.dp,
                    vertical = 4.dp,
                ).semantics(mergeDescendants = true) {}
                .clearAndSetSemantics { contentDescription = categoryText },
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
            )
            Text(
                text = categoryText,
                color = contentColor,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(start = 4.dp),
            )
        }
    }
}
