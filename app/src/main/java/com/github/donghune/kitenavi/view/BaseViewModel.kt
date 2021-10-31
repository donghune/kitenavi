package com.github.donghune.kitenavi.view

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class BaseViewModel : ViewModel() {

    companion object {
        private val TAG = "BaseViewModel"
    }

    private val mutableLoadState = MutableStateFlow<LoadState>(LoadState.Initialize)
    val loadState: StateFlow<LoadState> = mutableLoadState

    fun updateLoadState(loadState: LoadState) {
        mutableLoadState.value = loadState
    }
}

sealed class LoadState {
    object Initialize : LoadState()
    object Loading : LoadState()
    object Complete : LoadState()
    data class Error(val throwable: Throwable) : LoadState()
}
