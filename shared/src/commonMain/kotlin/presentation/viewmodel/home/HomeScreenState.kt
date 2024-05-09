package presentation.viewmodel.home

import domain.model.FinanceScreenModel

data class HomeScreenState(
    val financeScreenModel: FinanceScreenModel = FinanceScreenModel(),
    val showLoading: Boolean = false,
    val monthKey: String = "",
    val isInitialDataLoaded: Boolean = false,
)
