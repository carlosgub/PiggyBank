package com.carlosgub.myfinances.components.alertdialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import piggybank.components.generated.resources.Res
import piggybank.components.generated.resources.finance_alert_dialog_confirm_button
import piggybank.components.generated.resources.finance_alert_dialog_dismiss_button
import org.jetbrains.compose.resources.stringResource

@Composable
fun AlertDialog(
    dialogTitle: String,
    dialogText: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    icon: ImageVector? = null,
) {
    AlertDialog(
        icon = {
            icon?.let {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.clearAndSetSemantics { },
                )
            }
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                },
            ) {
                Text(stringResource(Res.string.finance_alert_dialog_confirm_button))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                },
            ) {
                Text(stringResource(Res.string.finance_alert_dialog_dismiss_button))
            }
        },
    )
}
