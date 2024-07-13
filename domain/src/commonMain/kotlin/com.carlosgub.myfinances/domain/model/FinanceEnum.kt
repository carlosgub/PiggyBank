package com.carlosgub.myfinances.domain.model

import myfinances.domain.generated.resources.Res
import myfinances.domain.generated.resources.finance_expense
import myfinances.domain.generated.resources.finance_income
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
