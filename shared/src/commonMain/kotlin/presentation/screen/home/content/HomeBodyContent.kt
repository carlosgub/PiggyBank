package presentation.screen.home.content

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import model.FinanceEnum
import model.FinanceScreenExpenses
import model.MonthExpense
import presentation.viewmodel.home.HomeScreenIntents
import presentation.viewmodel.home.HomeScreenState
import theme.ColorPrimary
import theme.Gray400
import theme.Gray600
import theme.MonthBudgetCardColor
import theme.spacing_1
import theme.spacing_16
import theme.spacing_2
import theme.spacing_3
import theme.spacing_4
import theme.spacing_6
import theme.spacing_8
import utils.toMoneyFormat
import utils.views.DataZero
import utils.views.ExpenseDivider
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun HomeBodyContent(
    modifier: Modifier,
    state: HomeScreenState,
    intents: HomeScreenIntents
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { 0 }),
        modifier = modifier
    ) {
        Card(
            shape = RoundedCornerShape(topStart = spacing_8, topEnd = spacing_8),
            colors = CardDefaults.cardColors(
                containerColor = MonthBudgetCardColor
            )
        ) {
            Column {
                CardMonthBudgetContent(
                    monthExpense = state.financeScreenModel.monthExpense
                )
                CardMonthFinanceContent(
                    state = state,
                    modifier = modifier,
                    intents = intents
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        visible = true
    }
}

@Composable
private fun CardMonthBudgetContent(
    monthExpense: MonthExpense
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(
                horizontal = spacing_6,
                vertical = spacing_8
            )
    ) {
        Row {
            Text(
                text = "Month Budget",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                monthExpense.incomeTotal.toMoneyFormat(),
                modifier = Modifier.padding(start = spacing_3),
                style = MaterialTheme.typography.bodyMedium,
                color = Gray600,
                fontWeight = FontWeight.Normal
            )
            Box(Modifier.weight(1f))
            Text(
                text = "${monthExpense.percentage}%",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
        CardMonthBudgetBar(
            monthExpense = monthExpense
        )
    }
}

@Composable
private fun CardMonthBudgetBar(monthExpense: MonthExpense) {
    val percentage = (monthExpense.percentage / 100.00).toFloat()
    var progress by rememberSaveable { mutableStateOf(0f) }
    val progressAnimation by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )
    LinearProgressIndicator(
        progress = { progressAnimation },
        color = ColorPrimary,
        modifier = Modifier.fillMaxWidth()
            .padding(top = spacing_2)
            .height(6.dp),
        strokeCap = StrokeCap.Round
    )
    LaunchedEffect(percentage) {
        delay(AnimationConstants.DefaultDurationMillis.milliseconds)
        progress = percentage
    }
}

@Composable
private fun CardMonthFinanceContent(
    state: HomeScreenState,
    modifier: Modifier,
    intents: HomeScreenIntents
) {
    val tabs = FinanceEnum.entries.toList()
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = spacing_8, topEnd = spacing_8),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = spacing_6, top = spacing_6, end = spacing_6)
        ) {
            var tabIndex by remember { mutableStateOf(FinanceEnum.EXPENSE) }
            CardMonthFinanceTabRow(
                tabs = tabs,
                tabIndex = tabIndex,
                onTabClicked = { financeEnum ->
                    tabIndex = financeEnum
                }
            )
            CardMonthFinanceTabContent(
                tabIndex = tabIndex,
                state = state,
                intents = intents
            )
        }
    }
}

@Composable
private fun CardMonthFinanceTabRow(
    tabs: List<FinanceEnum>,
    tabIndex: FinanceEnum,
    onTabClicked: (FinanceEnum) -> Unit
) {
    TabRow(
        selectedTabIndex = tabs.binarySearch(tabIndex),
        containerColor = Color.White,
        divider = {}
    ) {
        tabs.forEach { financeEnum ->
            Tab(
                text = {
                    Text(
                        text = financeEnum.financeName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                },
                selected = tabIndex == financeEnum,
                onClick = { onTabClicked(financeEnum) }
            )
        }
    }
}

@Composable
private fun CardMonthFinanceTabContent(
    tabIndex: FinanceEnum,
    state: HomeScreenState,
    intents: HomeScreenIntents
) {

    when (tabIndex) {
        FinanceEnum.EXPENSE -> {
            CardMonthFinanceCategoryContent(
                financeType = FinanceEnum.EXPENSE,
                expenses = state.financeScreenModel.expenses,
                intents = intents
            )
        }

        FinanceEnum.INCOME ->
            CardMonthFinanceCategoryContent(
                financeType = FinanceEnum.INCOME,
                expenses = state.financeScreenModel.income,
                intents = intents
            )
    }
}

@Composable
fun CardMonthFinanceCategoryContent(
    financeType: FinanceEnum,
    expenses: List<FinanceScreenExpenses>,
    intents: HomeScreenIntents
) {
    if (expenses.isNotEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.padding(top = spacing_3)
            ) {
                itemsIndexed(expenses) { count, expense ->
                    FinanceCategoryItem(
                        count = count, intents = intents, expense = expense
                    )
                }
            }
        }
    } else {
        DataZero(
            title = "Ooops! It's Empty",
            message = "Looks like you don't have anything in your ${financeType.financeName.lowercase()}",
            hasButton = true,
            valueToPass = financeType,
            onButtonClick = { financeEnum ->
                if (financeEnum == FinanceEnum.EXPENSE) {
                    intents.navigateToAddExpense()
                } else {
                    intents.navigateToAddIncome()
                }
            }
        )
    }
}

@Composable
private fun FinanceCategoryItem(
    count: Int,
    intents: HomeScreenIntents,
    expense: FinanceScreenExpenses
) {
    Column {
        if (count != 0) {
            ExpenseDivider(
                modifier = Modifier.padding(
                    start = spacing_16
                )
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    intents.navigateToMonthDetail(expense)
                }
                .padding(vertical = spacing_3)

        ) {
            ExpenseIconProgress(
                expense = expense
            )
            Column(
                modifier = Modifier.weight(1f).padding(start = spacing_4)
            ) {
                Text(
                    text = expense.category.categoryName,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${expense.percentage}% of budget",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = spacing_1),
                    color = Gray600,
                    fontWeight = FontWeight.Normal
                )
            }
            Column(
                modifier = Modifier.padding(start = spacing_4),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = (expense.amount / 100.0).toMoneyFormat(),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${expense.count} transactions",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = spacing_1),
                    color = Gray600,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun ExpenseIconProgress(
    expense: FinanceScreenExpenses
) {
    val percentage = (expense.percentage / 100.00).toFloat()
    Box(contentAlignment = Alignment.Center) {
        var progress by rememberSaveable { mutableStateOf(0f) }
        val progressAnimation by animateFloatAsState(
            targetValue = progress,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.size(56.dp),
            strokeWidth = 3.dp,
            color = Gray400
        )
        CircularProgressIndicator(
            progress = {
                progressAnimation
            },
            modifier = Modifier.size(56.dp),
            strokeWidth = 3.dp,
            color = expense.category.color
        )
        LaunchedEffect(Unit) {
            delay(AnimationConstants.DefaultDurationMillis.milliseconds)
            progress = percentage
        }
        Icon(
            imageVector = expense.category.icon,
            contentDescription = null,
            tint = Gray600,
            modifier = Modifier.size(28.dp)
        )
    }
}