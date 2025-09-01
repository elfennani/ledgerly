package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.repository.GroupRepository
import javax.inject.Inject

class ToggleGroupCollapsedUseCase @Inject constructor(private val groupRepository: GroupRepository) {
    suspend operator fun invoke(groupId: Int) {
        groupRepository.toggleGroupCollapsed(groupId)
    }
}