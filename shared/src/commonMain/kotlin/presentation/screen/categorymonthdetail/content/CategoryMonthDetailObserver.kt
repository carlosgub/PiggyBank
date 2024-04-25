package presentation.screen.categorymonthdetail.content

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import model.ExpenseScreenModel
import moe.tlaster.precompose.navigation.Navigator
import presentation.navigation.Screen
import presentation.viewmodel.monthDetail.CategoryMonthDetailScreenIntents
import presentation.viewmodel.monthDetail.CategoryMonthDetailScreenSideEffect
import utils.getCategoryEnumFromName

fun categoryMonthDetailObserver(
    sideEffect: CategoryMonthDetailScreenSideEffect,
    navigator: Navigator,
) {
    when (sideEffect) {
        is CategoryMonthDetailScreenSideEffect.NavigateToMonthDetail -> navigateToEditScreen(
            navigator = navigator,
            expenseScreenModel = sideEffect.expenseScreenModel,
        )
    }
}

private fun navigateToEditScreen(
    navigator: Navigator,
    expenseScreenModel: ExpenseScreenModel
) {
    navigator.navigate(
        Screen.EditScreen.createRoute(
            id = expenseScreenModel.id,
            financeName = getCategoryEnumFromName(name = expenseScreenModel.category).type.name
        )
    )
}
