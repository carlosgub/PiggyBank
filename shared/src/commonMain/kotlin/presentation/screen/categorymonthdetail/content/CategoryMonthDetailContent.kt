@file:OptIn(ExperimentalFoundationApi::class)

package presentation.screen.categorymonthdetail.content

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.carlosgub.myfinances.components.chart.FinanceLineChart
import com.carlosgub.myfinances.components.datazero.DataZero
import com.carlosgub.myfinances.components.divider.HorizontalDivider
import com.carlosgub.myfinances.components.loading.Loading
import com.carlosgub.myfinances.core.utils.toMoneyFormat
import com.carlosgub.myfinances.theme.Gray600
import com.carlosgub.myfinances.theme.Gray900
import com.carlosgub.myfinances.theme.White
import com.carlosgub.myfinances.theme.spacing_1
import com.carlosgub.myfinances.theme.spacing_2
import com.carlosgub.myfinances.theme.spacing_4
import com.carlosgub.myfinances.theme.spacing_6
import domain.model.ExpenseScreenModel
import myfinances.shared.generated.resources.Res
import myfinances.shared.generated.resources.category_month_detail_data_zero_message
import myfinances.shared.generated.resources.category_month_detail_data_zero_title
import org.jetbrains.compose.resources.stringResource
import presentation.viewmodel.categorymonthdetail.CategoryMonthDetailScreenState

@Composable
fun CategoryMonthDetailContent(
    paddingValues: PaddingValues,
    state: CategoryMonthDetailScreenState,
    expenseClicked: (ExpenseScreenModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(color = White)
            .padding(
                top = paddingValues.calculateTopPadding(),
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(0.35f),
        ) {
            CategoryMonthDetailHeader(
                state = state,
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().weight(0.65f),
        ) {
            CategoryMonthDetailBody(
                state = state,
                expenseClicked = expenseClicked,
            )
        }
    }
}

@Composable
fun CategoryMonthDetailBody(
    state: CategoryMonthDetailScreenState,
    expenseClicked: (ExpenseScreenModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.showLoading || state.isInitialDataLoaded.not()) {
        Loading()
    } else {
        Card(
            modifier = modifier
                .padding(top = spacing_2)
                .fillMaxSize(),
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp,
            ),
            colors = CardDefaults.cardColors(
                containerColor = White,
            ),
        ) {
            if (state.monthDetail.expenseScreenModel.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .background(color = White)
                        .fillMaxSize()
                        .padding(
                            top = spacing_6,
                            start = spacing_6,
                            end = spacing_6,
                        ),
                ) {
                    itemsIndexed(state.monthDetail.expenseScreenModel) { count, expense ->
                        Column {
                            if (count != 0) {
                                HorizontalDivider()
                            }
                            CategoryMonthExpenseItem(
                                expense = expense,
                                expenseClicked = expenseClicked,
                                modifier = Modifier.animateItemPlacement(
                                    animationSpec = tween(600),
                                ),
                            )
                        }
                    }
                }
            } else {
                DataZero<Any>(
                    title = stringResource(Res.string.category_month_detail_data_zero_title),
                    message = stringResource(Res.string.category_month_detail_data_zero_message),
                    modifier = Modifier
                        .background(color = White),
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CategoryMonthExpenseItem(
    expense: ExpenseScreenModel,
    expenseClicked: (ExpenseScreenModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    expenseClicked(expense)
                },
            )
            .padding(vertical = spacing_4),
    ) {
        Column(
            modifier = Modifier.weight(1f).padding(end = spacing_4),
        ) {
            Text(
                text = expense.note,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = Gray900,
            )
            Text(
                text = expense.date,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(top = spacing_1),
                color = Gray600,
                fontWeight = FontWeight.Normal,
            )
        }
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Top,
        ) {
            Text(
                text = (expense.amount / 100.0).toMoneyFormat(),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.End,
                color = Gray900,
            )
        }
    }
}

@Composable
private fun CategoryMonthDetailHeader(state: CategoryMonthDetailScreenState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (state.showLoading || state.isInitialDataLoaded.not()) {
            Loading()
        } else {
            Box {
                Column {
                    Spacer(modifier = Modifier.weight(0.2f))
                    FinanceLineChart(
                        state.monthDetail.daySpent,
                        withYChart = false,
                    )
                }
                Column {
                    Text(
                        text = (state.monthDetail.monthAmount / 100.0).toMoneyFormat(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Gray900,
                        modifier = Modifier.padding(spacing_6),
                    )
                }
            }
        }
    }
}
