package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.repository.GroupRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateGroupUseCase @Inject constructor(private val groupRepository: GroupRepository) {
    suspend operator fun invoke(id: Int, name: String) {
        val group = groupRepository.getGroupById(id).first()
        if (group != null) {
            val updatedGroup = group.copy(name = name)
            groupRepository.updateGroup(updatedGroup)
        }
    }
}