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
    coroutine: CoroutineScope,
    sideEffect: CategoryMonthDetailScreenSideEffect,
    navigator: Navigator,
    intents: CategoryMonthDetailScreenIntents
) {
    when (sideEffect) {
        is CategoryMonthDetailScreenSideEffect.NavigateToMonthDetail -> navigateToEditScreen(
            coroutine = coroutine,
            navigator = navigator,
            expenseScreenModel = sideEffect.expenseScreenModel,
            intents = intents
        )
    }
}

private fun navigateToEditScreen(
    coroutine: CoroutineScope,
    navigator: Navigator,
    expenseScreenModel: ExpenseScreenModel,
    intents: CategoryMonthDetailScreenIntents
) {
    coroutine.launch {
        val result = navigator.navigateForResult(
            Screen.EditScreen.createRoute(
                id = expenseScreenModel.id,
                financeName = getCategoryEnumFromName(name = expenseScreenModel.category).type.name
            )
        )
        if (result as Boolean) {
            intents.updateBackScreen()
            intents.getMonthDetail()
        }
    }
}