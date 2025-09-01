package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.repository.GroupRepository
import javax.inject.Inject

class DeleteGroupUseCase @Inject constructor(private val groupRepository: GroupRepository) {
    suspend operator fun invoke(groupId: Int) {
        groupRepository.deleteGroupById(groupId)
    }
}