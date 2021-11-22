package com.github.donghune.data.module

import com.github.donghune.data.local.AddressDao
import com.github.donghune.data.local.GroupDao
import com.github.donghune.data.network.service.AddressService
import com.github.donghune.data.repository.AddressRepositoryImpl
import com.github.donghune.data.repository.GroupRepositoryImpl
import com.github.donghune.domain.repository.AddressRepository
import com.github.donghune.domain.repository.GroupRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideAddressRepository(
        addressDao: AddressDao,
        addressService: AddressService
    ): AddressRepository {
        return AddressRepositoryImpl(
            addressDao,
            addressService
        )
    }

    @Provides
    fun provideGroupRepository(
        groupDao: GroupDao
    ): GroupRepository {
        return GroupRepositoryImpl(
            groupDao
        )
    }
}