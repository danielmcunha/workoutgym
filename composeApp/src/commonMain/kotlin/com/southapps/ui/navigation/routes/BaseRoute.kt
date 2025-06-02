package com.southapps.ui.navigation.routes

import com.southapps.core.extensions.fromJson
import com.southapps.core.extensions.toJson
import com.southapps.domain.master.entities.MasterSummary
import kotlinx.serialization.Serializable

object BaseRoute {

    @Serializable object Login
    @Serializable object Register
    @Serializable object MasterPlans
    @Serializable object AssociateSendInviteScreen
    @Serializable object AssociateListInvitesScreen

    @Serializable data class UserManageMaster(
        val data: String,
        val transitionKey: String
    ) {
        constructor(
            masterSummary: MasterSummary,
            transitionKey: String
        ) : this(masterSummary.toJson(), transitionKey)

        val masterSummary: MasterSummary
            get() = data.fromJson<MasterSummary>()
    }
}