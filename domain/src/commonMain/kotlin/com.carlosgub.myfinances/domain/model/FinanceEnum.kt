package com.carlosgub.myfinances.domain.model

import org.jetbrains.compose.resources.StringResource
import piggybank.domain.generated.resources.Res
import piggybank.domain.generated.resources.finance_expense
import piggybank.domain.generated.resources.finance_income

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
