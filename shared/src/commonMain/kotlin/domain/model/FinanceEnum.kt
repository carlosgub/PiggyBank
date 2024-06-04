@file:OptIn(ExperimentalResourceApi::class)

package domain.model

import myfinances.shared.generated.resources.Res
import myfinances.shared.generated.resources.finance_expense
import myfinances.shared.generated.resources.finance_income
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
