package com.carlosgub.myfinances.presentation.screen.home.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.carlosgub.myfinances.components.datazero.DataZero
import com.carlosgub.myfinances.components.divider.HorizontalDivider
import com.carlosgub.myfinances.core.utils.toMoneyFormat
import com.carlosgub.myfinances.domain.model.FinanceEnum
import com.carlosgub.myfinances.domain.model.FinanceScreenExpenses
import com.carlosgub.myfinances.domain.model.MonthExpense
import com.carlosgub.myfinances.presentation.viewmodel.home.HomeScreenIntents
import com.carlosgub.myfinances.presentation.viewmodel.home.HomeScreenState
import com.carlosgub.myfinances.theme.ColorPrimary
import com.carlosgub.myfinances.theme.Gray400
import com.carlosgub.myfinances.theme.Gray600
import com.carlosgub.myfinances.theme.MonthBudgetCardColor
import com.carlosgub.myfinances.theme.spacing_1
import com.carlosgub.myfinances.theme.spacing_16
import com.carlosgub.myfinances.theme.spacing_2
import com.carlosgub.myfinances.theme.spacing_3
import com.carlosgub.myfinances.theme.spacing_4
import com.carlosgub.myfinances.theme.spacing_6
import com.carlosgub.myfinances.theme.spacing_8
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import myfinances.presentation.generated.resources.Res
import myfinances.presentation.generated.resources.home_body_data_zero_message
import myfinances.presentation.generated.resources.home_body_data_zero_title
import myfinances.presentation.generated.resources.home_body_finance_category_item_budget_percentage
import myfinances.presentation.generated.resources.home_body_finance_category_item_count_transactions
import myfinances.presentation.generated.resources.home_body_monthly_budget
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun HomeBodyContent(
    state: HomeScreenState,
    intents: HomeScreenIntents,
    modifier: Modifier = Modifier,
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { 0 }),
        modifier = modifier,
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = spacing_8,
                topEnd = spacing_8,
            ),
            colors = CardDefaults.cardColors(
                containerColor = MonthBudgetCardColor,
            ),
        ) {
            Column {
                CardMonthBudgetContent(
                    monthExpense = state.financeScreenModel.monthExpense,
                )
                CardMonthFinanceContent(
                    state = state,
                    intents = intents,
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        visible = true
    }
}

@Composable
private fun CardMonthBudgetContent(monthExpense: MonthExpense) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(
                horizontal = spacing_6,
                vertical = spacing_8,
            ),
    ) {
        Row {
            Text(
                text = stringResource(Res.string.home_body_monthly_budget),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
            )
            Text(
                monthExpense.incomeTotal.toMoneyFormat(),
                modifier = Modifier.padding(start = spacing_3),
                style = MaterialTheme.typography.bodyMedium,
                color = Gray600,
                fontWeight = FontWeight.Normal,
            )
            Box(Modifier.weight(1f))
            Text(
                text = "${monthExpense.percentage}%",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
            )
        }
        CardMonthBudgetBar(
            monthExpense = monthExpense,
        )
    }
}

@Composable
private fun CardMonthBudgetBar(monthExpense: MonthExpense) {
    val percentage = (monthExpense.percentage / 100.00).toFloat()
    var progress by rememberSaveable { mutableFloatStateOf(0f) }
    val progressAnimation by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
    )
    LinearProgressIndicator(
        progress = { progressAnimation },
        color = ColorPrimary,
        modifier = Modifier.fillMaxWidth()
            .padding(top = spacing_2)
            .height(6.dp),
        strokeCap = StrokeCap.Round,
    )
    LaunchedEffect(percentage) {
        delay(AnimationConstants.DefaultDurationMillis.milliseconds)
        progress = percentage
    }
}

@Composable
private fun CardMonthFinanceContent(
    state: HomeScreenState,
    intents: HomeScreenIntents,
    modifier: Modifier = Modifier,
) {
    val tabs = FinanceEnum.entries.toImmutableList()
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = spacing_8, topEnd = spacing_8),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = spacing_6, top = spacing_6, end = spacing_6),
        ) {
            var tabIndex by rememberSaveable { mutableStateOf(FinanceEnum.EXPENSE) }
            CardMonthFinanceTabRow(
                tabs = tabs,
                tabIndex = tabIndex,
                onTabClicked = { financeEnum ->
                    tabIndex = financeEnum
                },
            )
            CardMonthFinanceTabContent(
                tabIndex = tabIndex,
                state = state,
                intents = intents,
            )
        }
    }
}

@Composable
private fun CardMonthFinanceTabRow(
    tabs: ImmutableList<FinanceEnum>,
    tabIndex: FinanceEnum,
    onTabClicked: (FinanceEnum) -> Unit,
) {
    TabRow(
        selectedTabIndex = tabs.binarySearch(tabIndex),
        containerColor = Color.White,
        divider = {},
    ) {
        tabs.forEach { financeEnum ->
            Tab(
                text = {
                    Text(
                        text = stringResource(financeEnum.financeName),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                    )
                },
                selected = tabIndex == financeEnum,
                onClick = { onTabClicked(financeEnum) },
            )
        }
    }
}

@Composable
private fun CardMonthFinanceTabContent(
    tabIndex: FinanceEnum,
    state: HomeScreenState,
    intents: HomeScreenIntents,
) {
    when (tabIndex) {
        FinanceEnum.EXPENSE -> {
            CardMonthFinanceCategoryContent(
                financeType = FinanceEnum.EXPENSE,
                expenses = state.financeScreenModel.expenses,
                intents = intents,
            )
        }

        FinanceEnum.INCOME ->
            CardMonthFinanceCategoryContent(
                financeType = FinanceEnum.INCOME,
                expenses = state.financeScreenModel.income,
                intents = intents,
            )
    }
}

@Composable
fun CardMonthFinanceCategoryContent(
    financeType: FinanceEnum,
    expenses: ImmutableList<FinanceScreenExpenses>,
    intents: HomeScreenIntents,
    modifier: Modifier = Modifier,
) {
    if (expenses.isNotEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
        ) {
            LazyColumn(
                modifier = Modifier.padding(top = spacing_3),
            ) {
                itemsIndexed(expenses) { count, expense ->
                    FinanceCategoryItem(
                        count = count,
                        intents = intents,
                        expense = expense,
                    )
                }
            }
        }
    } else {
        DataZero(
            title = stringResource(Res.string.home_body_data_zero_title),
            message = stringResource(
                resource = Res.string.home_body_data_zero_message,
                stringResource(financeType.financeName).lowercase(),
            ),
            hasButton = true,
            valueToPass = financeType,
            onButtonClick = { financeEnum ->
                if (financeEnum == FinanceEnum.EXPENSE) {
                    intents.navigateToAddExpense()
                } else {
                    intents.navigateToAddIncome()
                }
            },
        )
    }
}

@Composable
private fun FinanceCategoryItem(
    count: Int,
    intents: HomeScreenIntents,
    expense: FinanceScreenExpenses,
) {
    Column {
        if (count != 0) {
            HorizontalDivider(
                modifier = Modifier.padding(
                    start = spacing_16,
                ),
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    intents.navigateToMonthDetail(expense.category.name)
                }
                .padding(vertical = spacing_3),
        ) {
            ExpenseIconProgress(
                expense = expense,
            )
            Column(
                modifier = Modifier.weight(1f).padding(start = spacing_4),
            ) {
                Text(
                    text = stringResource(expense.category.categoryName),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = stringResource(
                        Res.string.home_body_finance_category_item_budget_percentage,
                        expense.percentage,
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = spacing_1),
                    color = Gray600,
                    fontWeight = FontWeight.Normal,
                )
            }
            Column(
                modifier = Modifier.padding(start = spacing_4),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = expense.amount.toMoneyFormat(),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = pluralStringResource(
                        Res.plurals.home_body_finance_category_item_count_transactions,
                        expense.count,
                        expense.count,
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = spacing_1),
                    color = Gray600,
                    fontWeight = FontWeight.Normal,
                )
            }
        }
    }
}

@Composable
fun ExpenseIconProgress(
    expense: FinanceScreenExpenses,
    modifier: Modifier = Modifier,
) {
    val percentage = (expense.percentage / 100.00).toFloat()
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        var progress by rememberSaveable { mutableFloatStateOf(0f) }
        val progressAnimation by animateFloatAsState(
            targetValue = progress,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        )
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.size(56.dp),
            strokeWidth = 3.dp,
            color = Gray400,
        )
        CircularProgressIndicator(
            progress = {
                progressAnimation
            },
            modifier = Modifier.size(56.dp),
            strokeWidth = 3.dp,
            color = expense.category.color,
        )
        LaunchedEffect(Unit) {
            delay(AnimationConstants.DefaultDurationMillis.milliseconds)
            progress = percentage
        }
        Icon(
            imageVector = expense.category.icon,
            contentDescription = null,
            tint = Gray600,
            modifier = Modifier.size(28.dp),
        )
    }
}
