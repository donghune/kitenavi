package com.github.donghune.kitenavi.domain

import com.github.donghune.kitenavi.model.local.Group
import com.github.donghune.kitenavi.model.local.GroupDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GroupRepository(
    private val groupDao: GroupDao
) {
    suspend fun insertGroup(groupName: String): Flow<Unit> {
        return flow {
            if (groupName.isEmpty()) {
                throw Exception("name is empty")
            }
            emit(groupDao.insert(Group(name = groupName)))
        }
    }

    suspend fun removeGroup(group: Group): Flow<Unit?> {
        return flow {
            emit(groupDao.delete(group))
        }
    }

    suspend fun getGroupList(): Flow<List<Group>> {
        return flow {
            emit(groupDao.getGroups())
        }
    }
}
