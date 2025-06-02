package com.southapps.domain.master.useCases

import com.southapps.data.session.MutableUserSession
import com.southapps.data.user.UserRepository
import com.southapps.domain.chicken.entities.ChickenSummary
import com.southapps.domain.common.useCase.UseCase

class GetChickensUseCase(
    private val repository: UserRepository,
    private val mutableUserSession: MutableUserSession
) : UseCase<Unit, List<ChickenSummary>>() {

    override suspend fun implementation(input: Unit): List<ChickenSummary> {
        return repository.getChickens().apply {
            mutableUserSession.updateChickens(this)
        }
    }
}