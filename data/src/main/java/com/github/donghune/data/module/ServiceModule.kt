package com.github.donghune.data.module

import com.github.donghune.data.network.client.RetrofitClient
import com.github.donghune.data.network.service.AddressService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    fun providerAddressService(): AddressService {
        return RetrofitClient.addressService
    }
}
