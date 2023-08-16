package presentation.viewmodel

import core.sealed.GenericState
import domain.usecase.GetFinanceUseCase
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import model.BirdImage
import model.Finance
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import kotlin.random.Random

class HomeViewModel(
    private val getFinanceUseCase: GetFinanceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<GenericState<Finance>>(GenericState.Initial)
    val uiState = _uiState.asStateFlow()

    init {
        updateImages()
    }

    private fun updateImages() {
        viewModelScope.launch {
            _uiState.emit(
                getFinanceUseCase.getFinance()
            )
        }
    }
}