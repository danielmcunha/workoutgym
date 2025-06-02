package com.southapps.core

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.southapps.ui.navigation.Navigator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S: UIState, A: UIAction>(
    internal val navigator: Navigator
): ViewModel(), LifecycleObserver {

    abstract val uiState: StateFlow<S>

    private val _uiAction = MutableSharedFlow<A>()
    val uiAction: SharedFlow<A> = _uiAction
    
    fun sendAction(action: A) {
        viewModelScope.launch {
            _uiAction.emit(action)
        }
    }
}