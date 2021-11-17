package com.github.donghune.data.repository

import com.github.donghune.data.local.AddressDao
import com.github.donghune.data.local.toAddress
import com.github.donghune.data.local.toAddressEntity
import com.github.donghune.data.network.response.toAddress
import com.github.donghune.data.network.service.AddressService
import com.github.donghune.domain.entity.Address
import com.github.donghune.domain.repository.AddressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddressRepositoryImpl(
    private val addressDao: AddressDao,
    private val service: AddressService
) : AddressRepository {

    override suspend fun searchAddress(query: String): Flow<List<Address>> {
        return flow {
            emit(service.search(query = query).document.map { it.toAddress() })
        }
    }

    override suspend fun insertAddress(
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
                    ).toAddressEntity()
                )
            )
        }
    }

    override suspend fun removeAddress(address: Address): Flow<Unit?> {
        return flow {
            emit(addressDao.delete(address.toAddressEntity()))
        }
    }

    override suspend fun getAddressList(groupId: Int): Flow<List<Address>> {
        return flow {
            emit(addressDao.getAddressListByGroup(groupId).map { it.toAddress() })
        }
    }
}