@file:OptIn(ExperimentalFoundationApi::class)

package com.carlosgub.myfinances.presentation.screen.month.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.carlosgub.myfinances.components.datazero.DataZero
import com.carlosgub.myfinances.components.divider.HorizontalDivider
import com.carlosgub.myfinances.components.loading.Loading
import com.carlosgub.myfinances.core.utils.toLocaleString
import com.carlosgub.myfinances.core.utils.toMonthKey
import com.carlosgub.myfinances.presentation.viewmodel.months.MonthsScreenState
import com.carlosgub.myfinances.theme.ColorPrimary
import com.carlosgub.myfinances.theme.White
import com.carlosgub.myfinances.theme.spacing_1_2
import com.carlosgub.myfinances.theme.spacing_2
import com.carlosgub.myfinances.theme.spacing_3
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.stringResource
import piggybank.presentation.generated.resources.Res
import piggybank.presentation.generated.resources.months_data_zero_message
import piggybank.presentation.generated.resources.months_data_zero_title

@Composable
fun MonthsContent(
    monthsScreenState: MonthsScreenState,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    onMonthClicked: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .background(color = White)
            .padding(
                top = paddingValues.calculateTopPadding(),
            )
            .fillMaxSize(),
    ) {
        if (monthsScreenState.showLoading) {
            Loading()
        } else {
            MonthsScreenSuccessContent(
                data = monthsScreenState.months,
                onMonthClicked = onMonthClicked,
            )
        }
    }
}

@Composable
private fun MonthsScreenSuccessContent(
    data: ImmutableMap<Int, List<LocalDateTime>>,
    onMonthClicked: (String) -> Unit,
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            initialOffsetX = {
                it
            },
        ),
        exit = slideOutHorizontally(
            targetOffsetX = {
                0
            },
        ),
        modifier = Modifier.fillMaxSize(),
    ) {
        if (data.isNotEmpty()) {
            MonthList(
                months = data,
                onClickItem = onMonthClicked,
            )
        } else {
            DataZero<Any>(
                title = stringResource(Res.string.months_data_zero_title),
                message = stringResource(Res.string.months_data_zero_message),
            )
        }
    }
    LaunchedEffect(Unit) {
        visible = true
    }
}

@Composable
fun MonthList(
    months: ImmutableMap<Int, List<LocalDateTime>>,
    modifier: Modifier = Modifier,
    onClickItem: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        months.forEach { entry ->
            stickyHeader {
                YearStickyHeader(entry.key)
            }
            items(entry.value) { localDateTime ->
                MonthItem(
                    localDateTime = localDateTime,
                    onClickItem = onClickItem,
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun YearStickyHeader(
    year: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = year.toString(),
        color = Color.White,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxSize()
            .background(color = ColorPrimary)
            .padding(spacing_1_2),
    )
}

@Composable
fun MonthItem(
    localDateTime: LocalDateTime,
    onClickItem: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxSize()
            .clickable {
                onClickItem(
                    localDateTime.toMonthKey(),
                )
            }
            .padding(
                horizontal = spacing_3,
                vertical = spacing_2,
            ),
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color = ColorPrimary),
            )
            Text(
                text = localDateTime.month.toLocaleString().take(1),
                color = White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )
        }

        Text(
            localDateTime.month.toLocaleString(),
            color = ColorPrimary,
            modifier = Modifier.fillMaxSize()
                .padding(spacing_2),
        )
    }
}
