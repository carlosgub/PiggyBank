package core.sealed

sealed class GenericState<out T> {
    object Loading : GenericState<Nothing>()
    object Initial : GenericState<Nothing>()
    data class Error(val message: String) : GenericState<Nothing>()
    data class Success<T>(val data: T) : GenericState<T>()
}
