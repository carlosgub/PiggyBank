@file:OptIn(ExperimentalResourceApi::class)

package domain.model

import myapplication.shared.generated.resources.Res
import myapplication.shared.generated.resources.finance_expense
import myapplication.shared.generated.resources.finance_income
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

enum class FinanceEnum(
    val financeName: StringResource,
) {
    EXPENSE(
        financeName = Res.string.finance_expense,
    ),
    INCOME(
        financeName = Res.string.finance_income,
    ),
}
