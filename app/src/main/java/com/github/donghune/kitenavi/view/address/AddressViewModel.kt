package com.github.donghune.kitenavi.view.address

import android.content.Context
import androidx.lifecycle.*
import com.github.donghune.kitenavi.domain.AddressRepository
import com.github.donghune.kitenavi.model.network.client.RetrofitClient
import com.github.donghune.kitenavi.model.local.Address
import com.github.donghune.kitenavi.model.local.AddressDatabase
import com.github.donghune.kitenavi.view.BaseViewModel
import com.github.donghune.kitenavi.view.LoadState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddressViewModel(
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

    class Factory(
        private val applicationContext: Context
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AddressViewModel(
                AddressRepository(
                    AddressDatabase.getInstance(applicationContext).addressDao(),
                    RetrofitClient.addressService
                )
            ) as T
        }

        fun build() = create(AddressViewModel::class.java)
    }
}
