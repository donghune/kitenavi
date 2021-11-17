package com.github.donghune.presenter.main

import androidx.lifecycle.viewModelScope
import com.github.donghune.domain.entity.Address
import com.github.donghune.domain.entity.Group
import com.github.donghune.domain.repository.AddressRepository
import com.github.donghune.domain.repository.GroupRepository
import com.github.donghune.kitenavi.view.BaseViewModel
import com.github.donghune.kitenavi.view.LoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
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

    companion object {
        private val TAG = "MainViewModel"
    }
}
