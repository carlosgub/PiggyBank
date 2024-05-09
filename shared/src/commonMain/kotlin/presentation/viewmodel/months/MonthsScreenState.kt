package presentation.viewmodel.months

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.datetime.LocalDateTime

data class MonthsScreenState(
    val months: ImmutableMap<Int, List<LocalDateTime>> = persistentMapOf(),
    val showLoading: Boolean = false
)
