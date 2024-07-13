package com.carlosgub.myfinances.presentation.viewmodel.createexpense

import com.carlosgub.myfinances.core.utils.toMoneyFormat
import com.carlosgub.myfinances.domain.model.CategoryEnum

data class CreateExpenseScreenState(
    val category: com.carlosgub.myfinances.domain.model.CategoryEnum = com.carlosgub.myfinances.domain.model.CategoryEnum.FOOD,
    val amountField: String = 0.0.toMoneyFormat(),
    val amount: Double = 0.0,
    val showDateError: Boolean = false,
    val showNoteError: Boolean = false,
    val showError: Boolean = false,
    val note: String = "",
    val date: String = "",
    val dateInMillis: Long = 0L,
    val showLoading: Boolean = false,
)
