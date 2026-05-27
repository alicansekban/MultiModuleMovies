package com.alican.multimodulemovies.utils

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<UiState, UiEvent, UiEffect>(
    protected val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // State management
    // using kotlin's new feature explicit backing fields
    val uiState: StateFlow<UiState>
        field = MutableStateFlow(initialState(savedStateHandle = savedStateHandle))

    // Effects management (one-time events to UI)
    private val _uiEffect = Channel<UiEffect>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    // Current state access
    protected val currentState: UiState
        get() = uiState.value

    /**
     * Handle UI events from the screen
     */
    abstract fun handleEvent(event: UiEvent)

    abstract fun initialState(savedStateHandle: SavedStateHandle): UiState

    /**
     * Update the UI state
     */
    protected fun updateState(newState: UiState) {
        uiState.update { newState }
    }

    /**
     * Update the UI state using a reducer function
     */
    protected fun updateState(reducer: UiState.() -> UiState) {
        uiState.value = currentState.reducer()
    }

    /**
     * Send a one-time effect to the UI
     */
    protected fun sendEffect(effect: UiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }

    /**
     * Save state to SavedStateHandle
     * Override this method to save specific state properties
     */
    protected open fun saveState() {
        // Override in child classes to save specific state
    }

    /**
     * Restore state from SavedStateHandle
     * Override this method to restore specific state properties
     */
    protected open fun restoreState(): UiState? {
        // Override in child classes to restore specific state
        return null
    }

    /**
     * Helper method to save a value to SavedStateHandle
     */
    protected fun <T> saveToHandle(key: String, value: T) {
        savedStateHandle[key] = value
    }

    /**
     * Helper method to get a value from SavedStateHandle
     */
    protected fun <T> getFromHandle(key: String): T? {
        return savedStateHandle.get<T>(key)
    }

    /**
     * Helper method to get a value from SavedStateHandle with default
     */
    protected fun <T> getFromHandle(key: String, defaultValue: T): T {
        return savedStateHandle.get<T>(key) ?: defaultValue
    }

    override fun onCleared() {
        super.onCleared()
        saveState()
    }
}