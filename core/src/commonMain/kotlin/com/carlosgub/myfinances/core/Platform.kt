package com.carlosgub.myfinances.core

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform