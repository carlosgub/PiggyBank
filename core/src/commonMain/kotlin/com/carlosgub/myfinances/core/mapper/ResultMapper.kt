package com.carlosgub.myfinances.core.mapper

import com.carlosgub.myfinances.core.network.ResponseResult
import com.carlosgub.myfinances.core.state.GenericState

object ResultMapper {
    fun <T> toGenericState(responseResult: ResponseResult<T>): GenericState<T> {
        return when (responseResult) {
            is ResponseResult.Success -> GenericState.Success(responseResult.data)
            is ResponseResult.Error -> GenericState.Error(responseResult.error.message.orEmpty())
        }
    }
}
