package com.github.donghune.presenter.search

import androidx.lifecycle.viewModelScope
import com.github.donghune.domain.entity.Address
import com.github.donghune.domain.repository.AddressRepository
import com.github.donghune.kitenavi.view.BaseViewModel
import com.github.donghune.kitenavi.view.LoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressSearchViewModel @Inject constructor(
    private val addressRepository: AddressRepository
) : BaseViewModel() {

    private val mutableQueryResult = MutableStateFlow<List<Address>>(listOf())
    val queryResult: StateFlow<List<Address>>
        get() = mutableQueryResult

    fun search(query: String) {
        viewModelScope.launch {
            addressRepository.searchAddress(query)
                .onStart { updateLoadState(LoadState.Loading) }
                .onEach { mutableQueryResult.value = it }
                .onCompletion { updateLoadState(LoadState.Complete) }
                .catch { updateLoadState(LoadState.Error(it)) }
                .launchIn(viewModelScope)
        }
    }

    fun insertAddress(
        name: String,
        latitude: Double,
        longitude: Double,
        groupId: Int
    ) {
        viewModelScope.launch {
            addressRepository.insertAddress(name, latitude, longitude, groupId)
                .onStart { updateLoadState(LoadState.Loading) }
                .onCompletion { updateLoadState(LoadState.Complete) }
                .catch { updateLoadState(LoadState.Error(it)) }
                .launchIn(viewModelScope)
        }
    }
}
