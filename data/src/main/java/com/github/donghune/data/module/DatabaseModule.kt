package com.github.donghune.data.module

import android.content.Context
import com.github.donghune.data.local.AddressDao
import com.github.donghune.data.local.AddressDatabase
import com.github.donghune.data.local.GroupDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideAddressDatabase(
        @ApplicationContext context: Context
    ): AddressDatabase {
        return AddressDatabase.getInstance(context)
    }

    @Provides
    fun provideGroupDao(
        addressDatabase: AddressDatabase
    ): GroupDao {
        return addressDatabase.groupDao()
    }

    @Provides
    fun provideAddressDao(
        addressDatabase: AddressDatabase
    ): AddressDao {
        return addressDatabase.addressDao()
    }
}