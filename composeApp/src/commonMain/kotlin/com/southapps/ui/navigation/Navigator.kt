package com.southapps.ui.navigation

import com.southapps.ui.navigation.routes.BottomNavigationRoute
import com.southapps.ui.navigation.routes.Route
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class Navigator {

    var startDestination: Any = BottomNavigationRoute.Home

    private val _route = Channel<Route>(Channel.BUFFERED)
    val route = _route.receiveAsFlow()

    private val _navResults = Channel<Pair<String, Any?>>(Channel.BUFFERED)
    val navResults = _navResults.receiveAsFlow()

    fun navigate(route: Any? = null, popBack: Boolean = false, clearStack: Boolean = false) {
        _route.trySend(Route(route = route, popBack = popBack, clearStack = clearStack))
    }

    fun navigateBack(resultKey: String? = null, resultValue: Any? = null) {
        if (resultKey != null) {
            _navResults.trySend(resultKey to resultValue)
        }

        navigate(popBack = true)
    }
}