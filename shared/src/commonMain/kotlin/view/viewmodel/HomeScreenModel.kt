package view.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.BirdImage
import view.viewmodel.state.BirdsUiState
import kotlin.random.Random

class HomeScreenModel : ScreenModel {
    private val _uiState = MutableStateFlow(BirdsUiState())
    val uiState = _uiState.asStateFlow()


    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    init {
        updateImages()
    }

    override fun onDispose() {
        super.onDispose()
        httpClient.close()
    }

    fun selectCategory(category: String) {
        coroutineScope.launch {
            _uiState.update {
                it.copy(selectedCategory = category)
            }
        }
    }

    fun updateImages() {
        coroutineScope.launch {
            val images = getImages()
            _uiState.update {
                it.copy(images = images.map {
                    it.copy(path = Random.nextInt(from = 1, until = 200).toString())
                })
            }
        }
    }

    private suspend fun getImages(): List<BirdImage> {
        return httpClient
            .get("https://sebi.io/demo-image-api/pictures.json")
            .body()
    }
}