package view.viewmodel

import core.sealed.GenericState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import model.BirdImage
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import kotlin.random.Random

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<GenericState<List<BirdImage>>>(GenericState.Initial)
    val uiState = _uiState.asStateFlow()
    private val _allList = MutableStateFlow<List<BirdImage>>(listOf())
    private val _selectedCategory = MutableStateFlow("")
    private val _categories = MutableStateFlow<Set<String>>(setOf())
    val categories = _categories.asStateFlow()

    init {
        updateImages()
    }


    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    override fun onCleared() {
        super.onCleared()
        httpClient.close()
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        viewModelScope.launch {
            emitUiStateSuccess()
        }
    }

    private fun updateImages() {
        viewModelScope.launch {
            val images = getImages()
            _allList.emit(
                images.map { birdImage ->
                    birdImage.copy(path = Random.nextInt(from = 1, until = 200).toString())
                }
            )
            emitUiStateSuccess()
        }
    }

    private suspend fun emitUiStateSuccess() {
        _categories.emit(
            _allList.value.map { it.category }.toSet()
        )
        _uiState.emit(
            GenericState.Success(
                _allList.value.filter { it.category == _selectedCategory.value }
            )
        )
    }

    private suspend fun getImages(): List<BirdImage> {
        return httpClient
            .get("https://sebi.io/demo-image-api/pictures.json")
            .body()
    }
}