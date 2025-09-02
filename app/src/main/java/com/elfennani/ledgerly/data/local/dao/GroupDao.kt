package com.elfennani.ledgerly.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.elfennani.ledgerly.data.local.entities.GroupEntity
import com.elfennani.ledgerly.data.local.relations.GroupWithCategories
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Query("SELECT * FROM `groups`")
    fun getAllGroupsFlow(): Flow<List<GroupEntity>>

    @Transaction
    @Query("SELECT * FROM `groups`")
    fun getAllGroupsWithCategoriesFlow(): Flow<List<GroupWithCategories>>

    @Query("SELECT * FROM `groups` WHERE id = :id")
    fun getGroupByIdFlow(id: Int): Flow<GroupEntity?>

    @Query("SELECT * FROM `groups` WHERE id = :id")
    suspend fun getGroupById(id: Int): GroupEntity?

    @Query("SELECT * FROM `groups`")
    suspend fun getAllGroups(): List<GroupEntity>

    @Insert
    suspend fun insertGroup(group: GroupEntity): Long

    @Update
    suspend fun updateGroup(group: GroupEntity)

    @Upsert
    suspend fun upsertGroup(group: GroupEntity)

    @Delete
    suspend fun deleteGroup(group: GroupEntity)

    @Query("DELETE FROM `groups` WHERE id = :id")
    suspend fun deleteGroupById(id: Int)

    @Query("UPDATE `groups` SET `index` = `index` + 1 WHERE `index` BETWEEN :startIndex AND :endIndex")
    suspend fun incrementIndicesInRange(startIndex: Int, endIndex: Int)

    @Query("UPDATE `groups` SET `index` = `index` - 1 WHERE `index` BETWEEN :startIndex AND :endIndex")
    suspend fun decrementIndicesInRange(startIndex: Int, endIndex: Int)

    @Query("UPDATE `groups` SET `index` = :newIndex WHERE `id` = :id")
    suspend fun updateGroupIndex(id: Int, newIndex: Int)

    // toggle collapsed state
    @Query("UPDATE `groups` SET `collapsed` = NOT `collapsed` WHERE id = :groupId")
    suspend fun toggleGroupCollapsed(groupId: Int)

    @Query("SELECT * FROM `groups` WHERE `index`=:index")
    suspend fun getGroupByIndex(index: Int): GroupEntity?

    @Transaction
    suspend fun moveGroup(fromIndex: Int, toIndex: Int) {
        if (fromIndex == toIndex) return

        val group = getGroupByIndex(fromIndex)

        if (fromIndex < toIndex) {
            // Moving down: decrement indices of groups between fromIndex and toIndex
            decrementIndicesInRange(fromIndex + 1, toIndex)
        } else {
            // Moving up: increment indices of groups between toIndex and fromIndex
            incrementIndicesInRange(toIndex, fromIndex - 1)
        }

        updateGroupIndex(group?.id!!, toIndex)
        // Finally, set the moved group's index to the new position

    }

    @Query("DELETE FROM categories WHERE groupId = :groupId")
    suspend fun deleteCategoriesByGroupId(groupId: Int)

    @Transaction
    suspend fun deleteGroupAndCategories(groupId: Int) {
        deleteCategoriesByGroupId(groupId)
        deleteGroupById(groupId)
    }
}