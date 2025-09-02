package com.elfennani.ledgerly.data.repository

import com.elfennani.ledgerly.data.local.dao.GroupDao
import com.elfennani.ledgerly.data.mappers.toDomain
import com.elfennani.ledgerly.data.mappers.toEntity
import com.elfennani.ledgerly.domain.model.Group
import com.elfennani.ledgerly.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val groupDao: GroupDao
) : GroupRepository {
    override val groupsFlow: Flow<List<Group>>
        get() = groupDao.getAllGroupsWithCategoriesFlow()
            .map { it.map { group -> group.toDomain() } }

    override suspend fun getGroupById(id: Int): Flow<Group?> {
        return groupDao.getGroupByIdFlow(id).map { it?.toDomain() }
    }

    override suspend fun insertGroup(group: Group): Long {
        val size = groupDao.getAllGroups().size
        return groupDao.insertGroup(group.toEntity().copy(index = size))
    }

    override suspend fun updateGroup(group: Group) {
        groupDao.updateGroup(group.toEntity())
    }

    override suspend fun deleteGroup(group: Group) {
        groupDao.deleteGroup(group.toEntity())
        groupDao.decrementIndicesInRange(group.index + 1, groupDao.getAllGroups().size)
    }

    override suspend fun deleteGroupById(id: Int) {
        val group = groupDao.getGroupById(id) ?: return
        groupDao.deleteGroupById(id)
        val groups = groupDao.getAllGroups()
        groupDao.decrementIndicesInRange(group.index + 1, groups.size)
    }

    override suspend fun moveGroup(fromIndex: Int, toIndex: Int) {
        groupDao.moveGroup(fromIndex, toIndex)
    }

    override suspend fun toggleGroupCollapsed(groupId: Int) {
        groupDao.toggleGroupCollapsed(groupId)
    }
}