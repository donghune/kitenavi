package com.github.donghune.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GroupDao {
    @Query("SELECT * FROM GroupEntity")
    suspend fun getGroups(): List<GroupEntity>

    @Insert
    suspend fun insert(group: GroupEntity)

    @Delete
    suspend fun delete(group: GroupEntity)
}