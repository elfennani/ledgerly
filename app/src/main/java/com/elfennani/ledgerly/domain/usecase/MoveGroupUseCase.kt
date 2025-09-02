package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.repository.GroupRepository
import javax.inject.Inject

class MoveGroupUseCase @Inject constructor(private val groupRepository: GroupRepository) {
    suspend operator fun invoke(fromIndex: Int, toIndex: Int) {
        groupRepository.moveGroup(fromIndex, toIndex)
    }
}