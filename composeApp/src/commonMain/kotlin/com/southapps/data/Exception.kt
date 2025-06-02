package com.southapps.data

open class AuthenticationException(message: String, cause: Throwable? = null) : Exception(message, cause)
class DataNotFoundException(message: String, cause: Throwable? = null) : Exception(message, cause)
class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause)
