package com.illeyrocci.centralcurrencies.domain.model

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(errorMessage: String, data: T? = null) : Resource<T>(message = errorMessage, data = data)
    class Loading<T> : Resource<T>()
}