package com.elfennani.ledgerly.domain.repository

import com.elfennani.ledgerly.domain.model.Group
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    val groupsFlow: Flow<List<Group>>
    suspend fun getGroupById(id: Int): Flow<Group?>
    suspend fun insertGroup(group: Group): Long
    suspend fun updateGroup(group: Group)
    suspend fun deleteGroup(group: Group)
    suspend fun deleteGroupById(id: Int)
    suspend fun moveGroup(fromIndex: Int, toIndex: Int)
    suspend fun toggleGroupCollapsed(groupId: Int)
}