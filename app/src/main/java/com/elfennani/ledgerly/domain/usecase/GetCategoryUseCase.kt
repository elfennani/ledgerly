package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.repository.CategoryRepository
import com.elfennani.ledgerly.domain.repository.GroupRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val groupRepository: GroupRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(categoryId: Int) =
        categoryRepository.getCategoryByIdFlow(categoryId).flatMapLatest { category ->
            val groupFlow = category?.let {
                groupRepository.getGroupById(it.groupId)
            }

            groupFlow?.map {
                it!! to category
            } ?: flowOf(null)
        }
}