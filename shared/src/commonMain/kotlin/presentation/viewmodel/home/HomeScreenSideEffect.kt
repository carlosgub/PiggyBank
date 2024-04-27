package presentation.viewmodel.home

import domain.model.FinanceScreenExpenses

sealed class HomeScreenSideEffect {
    data object NavigateToMonths : HomeScreenSideEffect()
    data class NavigateToMonthDetail(
        val financeScreenExpenses: FinanceScreenExpenses
    ) : HomeScreenSideEffect()
    data object NavigateToAddExpense : HomeScreenSideEffect()
    data object NavigateToAddIncome : HomeScreenSideEffect()
}
