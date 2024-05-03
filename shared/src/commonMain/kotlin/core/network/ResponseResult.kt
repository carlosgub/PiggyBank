package core.network

sealed class ResponseResult<out R> {
    data class Success<out T>(val data: T) : ResponseResult<T>()

    data class Error(val error: Throwable) : ResponseResult<Nothing>()
}
