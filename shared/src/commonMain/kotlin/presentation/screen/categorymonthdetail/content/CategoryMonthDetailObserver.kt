package presentation.screen.categorymonthdetail.content

import domain.model.ExpenseScreenModel
import domain.model.FinanceEnum
import moe.tlaster.precompose.navigation.Navigator
import presentation.navigation.Screen
import presentation.viewmodel.monthDetail.CategoryMonthDetailScreenSideEffect
import utils.getCategoryEnumFromName

fun categoryMonthDetailObserver(
    sideEffect: CategoryMonthDetailScreenSideEffect,
    navigator: Navigator,
) {
    when (sideEffect) {
        is CategoryMonthDetailScreenSideEffect.NavigateToMonthDetail ->
            navigateToEditScreen(
                navigator = navigator,
                expenseScreenModel = sideEffect.expenseScreenModel,
            )
    }
}

private fun navigateToEditScreen(
    navigator: Navigator,
    expenseScreenModel: ExpenseScreenModel,
) {
    val financeEnum = getCategoryEnumFromName(name = expenseScreenModel.category).type
    if (financeEnum == FinanceEnum.EXPENSE) {
        navigator.navigate(
            Screen.EditExpenseScreen.createRoute(
                id = expenseScreenModel.id,
            ),
        )
    } else {
        navigator.navigate(
            Screen.EditIncomeScreen.createRoute(
                id = expenseScreenModel.id,
            ),
        )
    }
}
