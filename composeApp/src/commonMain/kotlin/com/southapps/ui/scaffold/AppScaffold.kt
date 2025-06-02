package com.southapps.ui.scaffold

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.southapps.core.LocalUIActionRegister
import com.southapps.core.UIAction
import com.southapps.core.designSystem.HeaderContent
import com.southapps.core.designSystem.LocalHeaderContent
import com.southapps.core.designSystem.components.AppHeader
import com.southapps.core.designSystem.components.Dialog
import com.southapps.ui.navigation.AppNavHost
import com.southapps.ui.navigation.routes.BottomNavigationRoute
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlin.reflect.KClass

@Composable
private fun NavController.isCurrentRoute(kClass: KClass<out Any>): Boolean {
    val navBackStackEntry by currentBackStackEntryAsState()

    return navBackStackEntry?.destination?.hasRoute(kClass) ?: false
}

@Composable
fun AppScaffold(
    navController: NavHostController,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var uiAction by remember { mutableStateOf<SharedFlow<UIAction>?>(null) }
    var headerContent by remember { mutableStateOf<HeaderContent?>(null) }
    var showDialog by remember { mutableStateOf<UIAction.ShowDialog?>(null) }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val shouldShowBottomBar by remember(currentBackStackEntry) {
        derivedStateOf {
            BottomNavigationRoute.routes.any { route ->
                currentBackStackEntry?.destination?.hasRoute(route::class) == true
            }
        }
    }

    val register: (SharedFlow<UIAction>) -> Unit = { newUiAction ->
        uiAction = newUiAction
    }
    val updateHeader: (HeaderContent?) -> Unit = { header ->
        headerContent = header
    }

    LaunchedEffect(uiAction) {
        uiAction?.collectLatest { action ->
            when (action) {
                is UIAction.ShowSnackbar -> snackbarHostState.showSnackbar(action.message)
                is UIAction.ShowDialog -> showDialog = action
                else -> {

                }
            }
        }
    }

    if (showDialog != null) {
        Dialog(showDialog!!) {
            showDialog = null
        }
    }

    CompositionLocalProvider(
        LocalUIActionRegister provides register,
        LocalHeaderContent provides updateHeader

    ) {
        Scaffold(
            topBar = {
                headerContent?.let {
                    AppHeader(
                        it.title,
                        it.subtitle,
                        it.icons,
                        showBackButton = it.showBackButton,
                        onBackClick = {
                            navController.popBackStack()
                        })
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                if (shouldShowBottomBar) {
                    NavigationBar {
                        BottomNavigationRoute.items.forEach { screen ->
                            val selected = navController.isCurrentRoute(screen.route::class)
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    if (!selected) {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = screen.icon,
                                        contentDescription = screen.label
                                    )
                                },
                                label = { Text(screen.label) }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
