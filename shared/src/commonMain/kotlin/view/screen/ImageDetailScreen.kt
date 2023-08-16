package view.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.BirdImage

data class ImageDetailScreen(val birdImage: BirdImage) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Scaffold(
            topBar = {
                Toolbar(
                    title = birdImage.category,
                    onBack = {
                        navigator.pop()
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                KamelImage(
                    resource = asyncPainterResource("https://picsum.photos/id/${birdImage.path}/200"),
                    contentDescription = "${birdImage.category} by ${birdImage.author}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.0f)
                )
            }
        }
    }
}

@Composable
fun Toolbar(
    title: String,
    onBack: () -> Unit
) {
    TopAppBar(
        backgroundColor = Color.Black,
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 12.dp)
                        .size(36.dp)
                        .clickable { onBack() }
                        .padding(8.dp)
                )
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                )
            }
        }
    )
}