package com.github.donghune.presenter.group

import android.content.Context
import androidx.lifecycle.*
import com.github.donghune.domain.entity.Group
import com.github.donghune.domain.repository.GroupRepository
import com.github.donghune.kitenavi.model.local.AddressDatabase
import com.github.donghune.kitenavi.view.BaseViewModel
import com.github.donghune.kitenavi.view.LoadState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GroupViewModel(
    private val groupRepository: GroupRepository
) : BaseViewModel() {

    private val mutableGroupList = MutableStateFlow<List<Group>>(listOf())
    val groupList: StateFlow<List<Group>>
        get() = mutableGroupList

    fun insertGroup(groupName: String) {
        viewModelScope.launch {
            groupRepository.insertGroup(groupName)
                .onStart { updateLoadState(LoadState.Loading) }
                .onCompletion { updateLoadState(LoadState.Complete) }
                .catch { updateLoadState(LoadState.Error(it)) }
                .collect()
        }
    }

    fun removeGroup(group: Group) {
        viewModelScope.launch {
            groupRepository.removeGroup(group)
                .onStart { updateLoadState(LoadState.Loading) }
                .onCompletion { updateLoadState(LoadState.Complete) }
                .catch { updateLoadState(LoadState.Error(it)) }
                .collect()
        }
    }

    fun fetchGroupList() {
        viewModelScope.launch {
            groupRepository.getGroupList()
                .onStart { updateLoadState(LoadState.Loading) }
                .onEach { mutableGroupList.emit(it) }
                .onCompletion { updateLoadState(LoadState.Complete) }
                .catch { updateLoadState(LoadState.Error(it)) }
                .collect()
        }
    }

    class Factory(private val applicationContext: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GroupViewModel(
                GroupRepository(
                    AddressDatabase.getInstance(
                        applicationContext
                    ).groupDao()
                )
            ) as T
        }

        fun build(): GroupViewModel {
            return create(GroupViewModel::class.java)
        }
    }

    companion object {
        private const val TAG = "GroupViewModel"
    }
}
