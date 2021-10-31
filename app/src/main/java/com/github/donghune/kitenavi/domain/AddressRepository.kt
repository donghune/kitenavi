package com.github.donghune.kitenavi.domain

import com.github.donghune.kitenavi.model.local.Address
import com.github.donghune.kitenavi.model.local.AddressDao
import com.github.donghune.kitenavi.model.response.AddressSearchResponse
import com.github.donghune.kitenavi.model.service.AddressService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddressRepository(
    private val addressDao: AddressDao,
    private val service: AddressService
) {
    suspend fun searchAddress(query: String): Flow<AddressSearchResponse> {
        return flow {
            emit(service.search(query = query))
        }
    }

    suspend fun insertAddress(
        name: String,
        latitude: Double,
        longitude: Double,
        groupId: Int
    ): Flow<Unit> {
        return flow {
            if (name.isEmpty()) {
                throw Exception("name is empty")
            }
            emit(
                addressDao.insert(
                    Address(
                        name = name,
                        latitude = latitude,
                        longitude = longitude,
                        groupId = groupId
                    )
                )
            )
        }
    }

    suspend fun removeAddress(address: Address): Flow<Unit?> {
        return flow {
            emit(addressDao.delete(address))
        }
    }

    suspend fun getAddressList(groupId: Int): Flow<List<Address>> {
        return flow {
            emit(addressDao.getAddressListByGroup(groupId))
        }
    }
}
