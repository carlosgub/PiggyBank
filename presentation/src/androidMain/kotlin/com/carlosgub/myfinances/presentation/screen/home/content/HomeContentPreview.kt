package com.carlosgub.myfinances.presentation.screen.home.content

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.carlosgub.myfinances.domain.model.CategoryEnum
import com.carlosgub.myfinances.presentation.viewmodel.home.HomeScreenIntents
import com.carlosgub.myfinances.presentation.viewmodel.home.HomeScreenState
import kotlinx.coroutines.Job

val intents = object : HomeScreenIntents {
    override fun getFinanceStatus() = Job().also { it.complete() }
    override fun setMonthKey(monthKey: String) = Job().also { it.complete() }
    override fun navigateToMonths(): Job = Job().also { it.complete() }
    override fun navigateToMonthDetail(category: CategoryEnum): Job = Job().also { it.complete() }
    override fun navigateToAddExpense(): Job = Job().also { it.complete() }
    override fun navigateToAddIncome(): Job = Job().also { it.complete() }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun HomeContentPreview(
    @PreviewParameter(HomeContentPreviewParameterProvider::class) state: HomeScreenState,
) {
    HomeContent(
        paddingValues = PaddingValues(0.dp),
        state = state,
        intents = intents,
    )
}

