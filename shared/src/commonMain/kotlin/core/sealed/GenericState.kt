package core.sealed

sealed class GenericState<out T> {
    data class Error(val message: String) : GenericState<Nothing>()
    data class Success<T>(val data: T) : GenericState<T>()
}
