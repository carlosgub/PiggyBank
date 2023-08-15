package view.screen

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import di.homeModule
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.BirdImage
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import view.viewmodel.HomeScreenModel

class HomeScreen : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    fun BirdsPage(viewModel: HomeScreenModel) {
        val uiState by viewModel.uiState.collectAsState()
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                uiState.categories.forEach { category ->
                    Button(
                        onClick = {
                            viewModel.selectCategory(category)
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
            AnimatedVisibility(uiState.selectedImages.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.fillMaxSize().padding(horizontal = 5.dp),
                    content = {
                        items(uiState.selectedImages) { item ->
                            BirdImageCell(item)
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun BirdImageCell(image: BirdImage) {
        KamelImage(
            resource = asyncPainterResource("https://picsum.photos/id/${image.path}/200"),
            contentDescription = "${image.category} by ${image.author}",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().aspectRatio(1.0f)
        )
    }

    @Composable
    override fun Content() {
        BirdAppTheme {
            val screenModel = koinInject<HomeScreenModel>()
            BirdsPage(screenModel)
        }
    }
}

@Composable
fun BirdAppTheme(
    content: @Composable () -> Unit
) {
    KoinApplication(
        application = {
            modules(homeModule)
        }
    ){
        MaterialTheme(
            colors = MaterialTheme.colors.copy(primary = Color.Black),
            shapes = MaterialTheme.shapes.copy(
                small = AbsoluteCutCornerShape(0.dp),
                medium = AbsoluteCutCornerShape(0.dp),
                large = AbsoluteCutCornerShape(0.dp)
            )
        ) {
            content()
        }
    }
}