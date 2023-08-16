package view.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.sealed.GenericState
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.BirdImage
import org.koin.compose.koinInject
import view.viewmodel.HomeScreenModel

internal class HomeScreen : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        BirdsPage()
    }

    @Composable
    private fun BirdsPage(viewModel: HomeScreenModel = koinInject()) {
        LaunchedEffect(Unit) {
            viewModel.updateImages()
        }
        val categories = viewModel.categories.collectAsState().value
        val uiState by viewModel.state.collectAsState()
        when (uiState) {
            is GenericState.Success -> {
                HomeContent(
                    list = (uiState as GenericState.Success<List<BirdImage>>).data,
                    categories = categories,
                    categoryOnClick = { category ->
                        viewModel.selectCategory(category)
                    })
            }

            GenericState.Loading -> {

            }

            else -> Unit
        }

    }

    @Composable
    private fun HomeContent(
        list: List<BirdImage>,
        categories: Set<String>,
        categoryOnClick: (String) -> Unit
    ) {

        val navigator = LocalNavigator.currentOrThrow
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                categories.forEach { category ->
                    Button(
                        onClick = {
                            categoryOnClick(category)
                        },
                        modifier = Modifier.aspectRatio(1.0f).fillMaxSize().weight(1.0f),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            focusedElevation = 0.dp
                        )
                    ) {
                        Text(
                            text = category
                        )
                    }
                }
            }
            AnimatedVisibility(list.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.fillMaxSize().padding(horizontal = 5.dp),
                    content = {
                        items(list) { item ->
                            BirdImageCell(
                                birdImage = item,
                                onClick = {
                                    navigator.push(ImageDetailScreen(item))
                                }
                            )
                        }
                    }
                )
            }
        }
    }

    @Composable
    private fun BirdImageCell(
        birdImage: BirdImage,
        onClick: () -> Unit
    ) {
        KamelImage(
            resource = asyncPainterResource("https://picsum.photos/id/${birdImage.path}/200"),
            contentDescription = "${birdImage.category} by ${birdImage.author}",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().aspectRatio(1.0f).clickable {
                onClick()
            }
        )
    }
}
