package presentation.viewmodel.months

import androidx.annotation.VisibleForTesting
import core.sealed.GenericState
import domain.usecase.GetMonthsUseCase
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce

class MonthsViewModel(
    private val getMonthsUseCase: GetMonthsUseCase,
) : ViewModel(),
    ContainerHost<MonthsScreenState, MonthsScreenSideEffect>,
    MonthsScreenIntents {
    override val container: Container<MonthsScreenState, MonthsScreenSideEffect> =
        viewModelScope.container(MonthsScreenState()) {
            getMonths()
        }

    override fun getMonths(): Job =
        intent {
            showLoading()
            delay(200)
            getMonthsUseCase()
                .collect { result ->
                    when (result) {
                        is GenericState.Success -> {
                            setMonths(result.data)
                        }

                        else -> Unit
                    }
                }
        }

    override fun navigateToMonthDetail(monthKey: String): Job =
        intent {
            postSideEffect(MonthsScreenSideEffect.NavigateToMonthDetail(monthKey))
        }

    @VisibleForTesting
    fun setMonths(months: ImmutableMap<Int, List<LocalDateTime>>): Job =
        intent {
            reduce {
                state.copy(
                    showLoading = false,
                    months = months,
                )
            }
        }

    @VisibleForTesting
    fun showLoading(): Job =
        intent {
            reduce { state.copy(showLoading = true) }
        }
}
