package com.southapps.domain.common.exception

class UnknownAuthorizedException : Exception("Você não tem permissão para efetuar essa transação") {
    companion object {
        fun emit() {
            throw UnknownAuthorizedException()
        }
    }
}