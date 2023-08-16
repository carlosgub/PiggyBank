package presentation.components

sealed class Screen(val route: String) {

    object Overview : Screen(ROUTE_OVERVIEW)

    object Detail : Screen(ROUTE_DETAIL)

    companion object {
        private const val ROUTE_OVERVIEW = "overview"
        private const val ROUTE_DETAIL = "detail"
    }
}