package com.github.donghune.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AddressDao {
    @Query("SELECT * FROM AddressEntity WHERE groupId == (:groupId)")
    suspend fun getAddressListByGroup(groupId: Int): List<AddressEntity>

    @Insert
    suspend fun insert(address: AddressEntity)

    @Delete
    suspend fun delete(address: AddressEntity)
}