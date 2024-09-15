package com.carlosgub.myfinances.presentation.viewmodel.home

import com.carlosgub.myfinances.presentation.model.FinanceScreenModel

data class HomeScreenState(
    val financeScreenModel: FinanceScreenModel = FinanceScreenModel(),
    val showLoading: Boolean = false,
    val monthKey: String = "",
    val isInitialDataLoaded: Boolean = false,
)
