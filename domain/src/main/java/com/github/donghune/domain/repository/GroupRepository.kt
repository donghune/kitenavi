package com.github.donghune.domain.repository

import com.github.donghune.domain.entity.Group
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    suspend fun insertGroup(groupName: String): Flow<Unit>
    suspend fun removeGroup(group: Group): Flow<Unit?>
    suspend fun getGroupList(): Flow<List<Group>>
}

