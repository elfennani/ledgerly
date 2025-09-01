package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.repository.GroupRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetGroupsUseCase @Inject constructor(private val groupRepository: GroupRepository) {
    operator fun invoke() = groupRepository.groupsFlow.map { it.sortedBy { group -> group.index } }
}