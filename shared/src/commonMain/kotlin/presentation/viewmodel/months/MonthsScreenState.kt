package presentation.viewmodel.months

import kotlinx.datetime.LocalDateTime

data class MonthsScreenState(
    val months: Map<Int, List<LocalDateTime>> = mapOf(),
    val showLoading: Boolean = false
)
