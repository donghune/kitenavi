package com.github.donghune.domain.repository

import com.github.donghune.domain.entity.Address
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    suspend fun searchAddress(query: String): Flow<List<Address>>
    suspend fun insertAddress(
        name: String,
        latitude: Double,
        longitude: Double,
        groupId: Int
    ): Flow<Unit>
    suspend fun removeAddress(address: Address): Flow<Unit?>
    suspend fun getAddressList(groupId: Int): Flow<List<Address>>
}

