@file:OptIn(ExperimentalMaterial3Api::class)

package utils.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.MenuItem
import theme.ColorPrimary
import theme.spacing_2
import theme.view_10

@Composable
fun Toolbar(
    backgroundColor: Color = ColorPrimary,
    title: String,
    hasNavigationIcon: Boolean = false,
    navigationIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    navigation: () -> Unit = {},
    leftIcon: ImageVector? = null,
    onLeftIconPressed: () -> Unit = {},
    contentColor: Color = Color.White,
    dropDownMenu: Boolean = false,
    dropDownIcon: ImageVector? = null,
    dropDownItems: List<MenuItem> = listOf()
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = TextStyle(
                    color = contentColor,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = backgroundColor,
            navigationIconContentColor = contentColor,
            titleContentColor = contentColor,
            actionIconContentColor = contentColor,
            scrolledContainerColor = backgroundColor
        ),
        navigationIcon = {
            if (hasNavigationIcon) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(spacing_2)
                        .size(40.dp)
                        .clickable { navigation() }
                        .padding(spacing_2),
                    tint = contentColor
                )
            }
        },
        actions = {
            leftIcon?.let {
                Icon(
                    imageVector = leftIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(spacing_2)
                        .size(view_10)
                        .clickable { onLeftIconPressed() }
                        .padding(spacing_2),
                    tint = contentColor
                )
            }
            if (dropDownMenu) {
                var expanded by remember { mutableStateOf(false) }
                Column {
                    Icon(
                        imageVector = dropDownIcon ?: Icons.Filled.MoreVert,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(spacing_2)
                            .size(view_10)
                            .clickable { expanded = true }
                            .padding(spacing_2),
                        tint = contentColor
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        },
                        modifier = Modifier.background(Color.White)
                    ) {
                        dropDownItems.forEach {
                            DropdownMenuItem(
                                it.icon,
                                it.name,
                                onItemClicked = {
                                    expanded = false
                                    it.onItemClicked()
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}
