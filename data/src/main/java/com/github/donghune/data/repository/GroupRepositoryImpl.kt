package com.github.donghune.data.repository

import com.github.donghune.data.local.GroupDao
import com.github.donghune.data.local.GroupEntity
import com.github.donghune.data.local.toGroup
import com.github.donghune.data.local.toGroupEntity
import com.github.donghune.domain.entity.Group
import com.github.donghune.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val groupDao: GroupDao
) : GroupRepository {
    override suspend fun insertGroup(groupName: String): Flow<Unit> {
        return flow {
            if (groupName.isEmpty()) {
                throw Exception("name is empty")
            }
            emit(groupDao.insert(GroupEntity(name = groupName)))
        }
    }

    override suspend fun removeGroup(group: Group): Flow<Unit?> {
        return flow {
            emit(groupDao.delete(group.toGroupEntity()))
        }
    }

    override suspend fun getGroupList(): Flow<List<Group>> {
        return flow {
            emit(groupDao.getGroups().map { it.toGroup() })
        }
    }
}