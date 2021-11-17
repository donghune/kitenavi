package com.github.donghune.presenter.address

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
class AddressViewModel @Inject constructor(
    private val addressRepository: AddressRepository
) : BaseViewModel() {

    private val mutableAddressList = MutableStateFlow<List<Address>>(listOf())
    val addressList: StateFlow<List<Address>>
        get() = mutableAddressList

    fun removeAddress(address: Address) {
        viewModelScope.launch {
            addressRepository.removeAddress(address)
                .onStart { updateLoadState(LoadState.Loading) }
                .onCompletion { updateLoadState(LoadState.Complete) }
                .catch { updateLoadState(LoadState.Error(it)) }
                .launchIn(viewModelScope)
        }
    }

    fun fetchAddressList(groupId: Int) {
        viewModelScope.launch {
            addressRepository.getAddressList(groupId)
                .onStart { updateLoadState(LoadState.Loading) }
                .onEach { mutableAddressList.value = it }
                .onCompletion { updateLoadState(LoadState.Complete) }
                .catch { updateLoadState(LoadState.Error(it)) }
                .launchIn(viewModelScope)
        }
    }

}
