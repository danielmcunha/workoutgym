package com.southapps.core.di

import com.southapps.data.session.MutableUserSession
import com.southapps.data.session.UserSession
import com.southapps.data.session.UserSessionManager
import com.southapps.preferenceModule
import com.southapps.ui.navigation.Navigator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val sessionModule = module {
    single<MutableUserSession> { UserSessionManager() }
    single<UserSession> { get<MutableUserSession>() }
}

val appModule = listOf(
    viewModelModule,
    useCaseModule,
    repositoryModule,
    preferenceModule,
    sessionModule,
    module {
        singleOf(::Navigator)
    }
)
