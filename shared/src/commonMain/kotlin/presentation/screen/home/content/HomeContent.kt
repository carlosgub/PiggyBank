@file:OptIn(ExperimentalMaterialApi::class)

package presentation.screen.home.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import presentation.viewmodel.home.HomeScreenIntents
import presentation.viewmodel.home.HomeScreenState
import theme.ColorPrimary

@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    state: HomeScreenState,
    intents: HomeScreenIntents
) {
    val scrollState = rememberScrollState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.showLoading && state.isInitialDataLoaded,
        onRefresh = { intents.getFinanceStatus() }
    )
    Column(
        modifier = Modifier
            .padding(top = paddingValues.calculateTopPadding())
            .fillMaxSize()
            .background(ColorPrimary)
    ) {
        BoxWithConstraints {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
                    .verticalScroll(scrollState)
            ) {
                if (state.isInitialDataLoaded) {
                    Column(
                        modifier = Modifier
                            .height(this@BoxWithConstraints.maxHeight)
                            .fillMaxSize()
                            .background(ColorPrimary)
                    ) {
                        HomeHeaderContent(
                            modifier = Modifier
                                .weight(0.3f)
                                .fillMaxSize(),
                            financeScreenModel = state.financeScreenModel
                        )
                        HomeBodyContent(
                            modifier = Modifier
                                .weight(0.7f)
                                .fillMaxSize(),
                            state = state,
                            intents = intents
                        )
                    }
                }
                PullRefreshIndicator(
                    refreshing = state.showLoading && state.isInitialDataLoaded,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}