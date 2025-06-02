package com.southapps.domain.common.exception

class GenericException : Exception("Ops, não foi possível efetuar a transação") {
    companion object {
        fun emit() {
            throw GenericException()
        }
    }
}