package com.github.donghune.kitenavi.view.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.donghune.kitenavi.domain.AddressRepository
import com.github.donghune.kitenavi.model.network.client.RetrofitClient
import com.github.donghune.kitenavi.model.local.AddressDatabase
import com.github.donghune.kitenavi.model.network.response.Document
import com.github.donghune.kitenavi.view.BaseViewModel
import com.github.donghune.kitenavi.view.LoadState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddressSearchViewModel(
    private val addressRepository: AddressRepository
) : BaseViewModel() {

    private val mutableQueryResult = MutableStateFlow<List<Document>>(listOf())
    val queryResult: StateFlow<List<Document>>
        get() = mutableQueryResult

    fun search(query: String) {
        viewModelScope.launch {
            addressRepository.searchAddress(query)
                .onStart { updateLoadState(LoadState.Loading) }
                .onEach { mutableQueryResult.value = it.documents }
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

    class Factory(
        private val applicationContext: Context
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AddressSearchViewModel(
                AddressRepository(
                    AddressDatabase.getInstance(applicationContext).addressDao(),
                    RetrofitClient.addressService
                )
            ) as T
        }

        fun build(): AddressSearchViewModel {
            return create(AddressSearchViewModel::class.java)
        }
    }
}
