package core.mapper

import core.network.ResponseResult
import core.sealed.GenericState

object ResultMapper {

    fun<T> toGenericState(responseResult: ResponseResult<T>): GenericState<T> {
        return when (responseResult) {
            is ResponseResult.Success -> GenericState.Success(responseResult.data)
            is ResponseResult.Error -> GenericState.Error(responseResult.error.message.orEmpty())
        }
    }
}
