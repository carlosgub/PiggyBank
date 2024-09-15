package com.carlosgub.myfinances.core.mapper

fun interface Mapper<in From, out To> {
    fun map(from: From): To
}
