@file:OptIn(ExperimentalFoundationApi::class)

package presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import core.sealed.GenericState
import kotlinx.datetime.LocalDateTime
import model.HomeArgs
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.viewmodel.viewModel
import presentation.navigation.Screen
import presentation.viewmodel.MonthsScreenViewModel
import theme.ColorPrimary
import theme.White
import theme.spacing_1_2
import theme.spacing_2
import theme.spacing_3
import utils.get
import utils.toMonthString
import utils.views.ExpenseDivider
import utils.views.Loading
import utils.views.Toolbar

@Composable
fun MonthsScreen(
    navigator: Navigator
) {
    val viewModel = viewModel(MonthsScreenViewModel::class) {
        MonthsScreenViewModel(get())
    }
    Scaffold(
        topBar = {
            MonthsToolbar(
                onBack = {
                    navigator.goBack()
                }
            )
        }
    ) { paddingValues ->
        MonthsObserver(navigator, viewModel, paddingValues)
    }
}

@Composable
private fun MonthsObserver(
    navigator: Navigator,
    viewModel: MonthsScreenViewModel,
    paddingValues: PaddingValues
) {
    AnimatedContent(
        targetState = viewModel.uiState.collectAsStateWithLifecycle().value,
        transitionSpec = {
            (
                fadeIn(animationSpec = tween(300, delayMillis = 90)) +
                    slideIntoContainer(
                        animationSpec = tween(300, delayMillis = 90),
                        towards = AnimatedContentTransitionScope.SlideDirection.Left
                    )
                )
                .togetherWith(fadeOut(animationSpec = tween(90)))
        }
    ) { targetState ->
        when (targetState) {
            is GenericState.Success -> {
                MonthsContent(
                    paddingValues,
                    content = {
                        MonthList(
                            months = targetState.data,
                            onClickItem = { monthKey ->
                                navigator.navigate(
                                    Screen.Home.createRoute(
                                        HomeArgs(
                                            monthKey
                                        )
                                    )
                                )
                            }
                        )
                    }
                )
            }

            GenericState.Loading -> {
                MonthsContent(
                    paddingValues,
                    content = {
                        Loading()
                    }
                )
            }

            GenericState.Initial -> {
                viewModel.getMonths()
            }

            else -> Unit
        }
    }
}

@Composable
fun MonthList(
    months: Map<Int, List<LocalDateTime>>,
    onClickItem: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        months.forEach { entry ->
            stickyHeader {
                YearStickyHeader(entry.key)
            }
            items(entry.value) { localDateTime ->
                MonthItem(
                    localDateTime = localDateTime,
                    onClickItem = onClickItem
                )
                ExpenseDivider()
            }
        }
    }
}

@Composable
fun YearStickyHeader(year: Int) {
    Text(
        text = year.toString(),
        color = Color.White,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxSize()
            .background(ColorPrimary)
            .padding(spacing_1_2)
    )
}

@Composable
fun MonthItem(
    localDateTime: LocalDateTime,
    onClickItem: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                onClickItem(
                    "${localDateTime.month.toMonthString()}${localDateTime.year}"
                )
            }
            .padding(
                horizontal = spacing_3,
                vertical = spacing_2
            )
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(ColorPrimary)
            )
            Text(
                localDateTime.month.name.take(1),
                color = White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            localDateTime.month.name,
            color = ColorPrimary,
            modifier = Modifier.fillMaxSize()
                .padding(spacing_2)
        )
    }
}

@Composable
fun MonthsContent(
    paddingValues: PaddingValues,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .background(White)
            .padding(
                top = paddingValues.calculateTopPadding()
            )
            .fillMaxSize()
    ) {
        content()
    }
}

@Composable
private fun MonthsToolbar(
    onBack: () -> Unit
) {
    Toolbar(
        backgroundColor = Color.White,
        hasNavigationIcon = true,
        navigation = onBack,
        contentColor = Color.Black,
        title = "Months"
    )
}
