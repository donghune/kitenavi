package com.github.donghune.presenter.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.donghune.data.network.client.RetrofitClient
import com.github.donghune.domain.entity.Address
import com.github.donghune.domain.entity.Group
import com.github.donghune.domain.repository.AddressRepository
import com.github.donghune.domain.repository.GroupRepository
import com.github.donghune.kitenavi.model.local.AddressDatabase
import com.github.donghune.kitenavi.view.BaseViewModel
import com.github.donghune.kitenavi.view.LoadState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val groupRepository: GroupRepository,
    private val addressRepository: AddressRepository
) : BaseViewModel() {

    private val mutableGroupList = MutableStateFlow<List<Group>>(listOf())
    val groupList: StateFlow<List<Group>>
        get() = mutableGroupList

    private val mutableAddressList = MutableStateFlow<List<Address>>(listOf())
    val addressList: StateFlow<List<Address>>
        get() = mutableAddressList

    fun fetchGroupList() {
        viewModelScope.launch {
            groupRepository.getGroupList()
                .onStart { updateLoadState(LoadState.Loading) }
                .onEach { mutableGroupList.value = it }
                .onCompletion { updateLoadState(LoadState.Complete) }
                .catch { updateLoadState(LoadState.Error(it)) }
                .launchIn(viewModelScope)
        }
    }

    fun fetchAddressListByGroup(group: Group) {
        viewModelScope.launch {
            addressRepository.getAddressList(group.id)
                .onStart { updateLoadState(LoadState.Loading) }
                .onEach { mutableAddressList.emit(it) }
                .onCompletion { updateLoadState(LoadState.Complete) }
                .catch { updateLoadState(LoadState.Error(it)) }
                .launchIn(viewModelScope)
        }
    }

    class Factory(
        val context: Context
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(
                GroupRepository(AddressDatabase.getInstance(context).groupDao()),
                AddressRepository(
                    AddressDatabase.getInstance(context).addressDao(),
                    RetrofitClient.addressService
                )
            ) as T
        }

        fun build(): MainViewModel {
            return create(MainViewModel::class.java)
        }
    }

    companion object {
        private val TAG = "MainViewModel"
    }
}
