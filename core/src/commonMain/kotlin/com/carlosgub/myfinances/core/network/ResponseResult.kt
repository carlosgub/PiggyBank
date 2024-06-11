package com.carlosgub.myfinances.core.network

sealed class ResponseResult<out T> {
    data class Success<out T>(val data: T) : ResponseResult<T>()

    data class Error(val error: Throwable) : ResponseResult<Nothing>()
}
