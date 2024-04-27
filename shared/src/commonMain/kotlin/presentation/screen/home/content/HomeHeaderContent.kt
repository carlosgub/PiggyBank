@file:OptIn(ExperimentalFoundationApi::class)

package presentation.screen.home.content

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import domain.model.FinanceScreenModel
import theme.Gray900
import theme.spacing_1_2
import theme.spacing_2
import theme.spacing_4
import utils.toMoneyFormat
import utils.views.chart.FinanceBarChart

@Composable
fun HomeHeaderContent(
    modifier: Modifier,
    financeScreenModel: FinanceScreenModel
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutine = rememberCoroutineScope()
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        modifier = modifier
    ) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false
        ) { page ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                HomeHeaderLeftIcon(
                    currentPage = pagerState.currentPage,
                    onIconClicked = {
                        coroutine.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                )
                if (page == 0) {
                    HomeHeaderFirstPage(
                        month = financeScreenModel.month,
                        monthAmount = financeScreenModel.expenseAmount,
                        modifier = Modifier
                            .weight(1f)
                    )
                } else {
                    HomeHeaderSecondPage(
                        daySpent = financeScreenModel.daySpent,
                        modifier = Modifier
                            .weight(1f)
                    )
                }
                HomeHeaderRightIcon(
                    currentPage = pagerState.currentPage,
                    pageCount = pagerState.pageCount,
                    monthAmount = financeScreenModel.expenseAmount,
                    onIconClicked = {
                        coroutine.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        visible = true
    }
}

@Composable
private fun HomeHeaderLeftIcon(
    currentPage: Int,
    onIconClicked: () -> Unit
) {
    IconButton(
        onClick = { if (currentPage > 0) onIconClicked() },
        content = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowLeft,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .padding(horizontal = spacing_4)
                    .clip(CircleShape)
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = Color.White
                        ),
                        shape = CircleShape
                    )
                    .padding(spacing_2)
            )
        },
        modifier = Modifier
            .alpha(if (currentPage > 0) 1f else 0f)
    )
}

@Composable
private fun HomeHeaderFirstPage(
    month: Month,
    monthAmount: Long,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = month.name,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White
        )
        Text(
            text = (monthAmount / 100.0).toMoneyFormat(),
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier.padding(top = spacing_4)
        )
    }
}

@Composable
private fun HomeHeaderSecondPage(
    daySpent: Map<LocalDateTime, Long>,
    modifier: Modifier
) {
    var overlayData by remember { mutableStateOf("") }
    Box(
        modifier = modifier
    ) {
        OverlayData(
            overlayData = overlayData,
            modifier = Modifier.align(Alignment.TopEnd)
        )
        FinanceBarChart(
            daySpent = daySpent,
            barColor = Color.White,
            withYChart = true,
            contentColor = Color.White,
            modifier = Modifier
                .fillMaxSize(),
            onOverlayData = { data ->
                overlayData = data
            }
        )
    }
}

@Composable
private fun OverlayData(
    overlayData: String,
    modifier: Modifier
) {
    if (overlayData.isNotEmpty()) {
        Box(
            Modifier
                .clip(RoundedCornerShape(spacing_4))
                .background(Gray900)
                .padding(
                    horizontal = spacing_2,
                    vertical = spacing_1_2
                )
                .then(modifier)
        ) {
            AnimatedContent(
                targetState = overlayData,
                transitionSpec = {
                    fadeIn(animationSpec = tween(durationMillis = 300)) togetherWith
                        fadeOut(animationSpec = tween(durationMillis = 300))
                },
                contentAlignment = Alignment.Center
            ) { overlayData ->
                Text(
                    text = overlayData,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun HomeHeaderRightIcon(
    currentPage: Int,
    pageCount: Int,
    monthAmount: Long,
    onIconClicked: () -> Unit
) {
    val isEnabled = currentPage + 1 < pageCount && monthAmount > 0
    IconButton(
        onClick = { if (isEnabled) onIconClicked() },
        content = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .padding(horizontal = spacing_4)
                    .clip(CircleShape)
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = Color.White
                        ),
                        shape = CircleShape
                    )
                    .padding(spacing_2)
            )
        },
        modifier = Modifier
            .alpha(if (isEnabled) 1f else 0f)
    )
}
