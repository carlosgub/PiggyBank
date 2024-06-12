package com.carlosgub.myfinances.components.toolbar.parameter

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val name: String,
    val icon: ImageVector,
    val onItemClicked: () -> Unit,
)
