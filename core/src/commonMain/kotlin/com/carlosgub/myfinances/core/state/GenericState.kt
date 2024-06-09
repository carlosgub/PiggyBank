package com.carlosgub.myfinances.core.state

sealed class GenericState<out T> {
    data class Success<out T>(val data: T) : GenericState<T>()
    data class Error(val message: String) : GenericState<Nothing>()
}
