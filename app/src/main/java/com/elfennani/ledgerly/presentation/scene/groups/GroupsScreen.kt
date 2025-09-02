package com.elfennani.ledgerly.presentation.scene.groups

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.Group
import com.elfennani.ledgerly.presentation.scene.groups.component.CreateGroupModal
import com.elfennani.ledgerly.presentation.scene.groups.component.EditGroupModal
import com.elfennani.ledgerly.presentation.scene.groups.component.GroupItem
import com.elfennani.ledgerly.presentation.theme.AppTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun GroupsScreen(
    navController: NavController,
    viewModel: GroupsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    GroupsScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroupsScreen(
    state: GroupsUiState,
    onEvent: (GroupsEvent) -> Unit = { },
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Groups") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(painterResource(R.drawable.arrow_left), null)
                    }
                }
            )
        }
    ) { innerPadding ->
        val createGroupModal = rememberModalBottomSheetState(true)
        val editGroupModal = rememberModalBottomSheetState(true)
        val lazyState = rememberLazyListState()
        val scope = rememberCoroutineScope()
        val dragState = rememberGroupDragState(
            lazyState,
            state.groups,
            innerPadding.calculateTopPadding() + 16.dp
        )

        var lastDraggedGroupId by remember { mutableStateOf<Int?>(null) }
        val density = LocalDensity.current

        if (state.isAddGroupDialogVisible) {
            CreateGroupModal(
                sheetState = createGroupModal,
                formState = state.groupFormState,
                onEvent = onEvent,
                onDismissRequest = {
                    scope.launch {
                        createGroupModal.hide()
                    }.invokeOnCompletion { onEvent(GroupsEvent.DismissAddGroupModal) }
                }
            )
        }

        if (state.editingGroupId != null) {
            EditGroupModal(
                sheetState = editGroupModal,
                formState = state.groupFormState,
                onEvent = onEvent,
                groupId = state.editingGroupId,
                onDismissRequest = {
                    scope.launch {
                        editGroupModal.hide()
                    }.invokeOnCompletion { onEvent(GroupsEvent.DismissEditGroupModal) }
                }
            )
        }

        if (state.isLoading) {
            Column(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .dragGroupHandler(dragState, state.groups) { from, to ->
                        lastDraggedGroupId = state.groups.getOrNull(from)?.id
                        onEvent(GroupsEvent.MoveGroup(from, to))
                    }
                    .padding(innerPadding),
                state = lazyState,
                contentPadding = PaddingValues(16.dp),
            ) {
                if (state.groups.isEmpty()) {
                    item {
                        Text(
                            text = "No groups available.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp)
                        )
                    }
                }
                items(state.groups, key = { it.id }) { group ->
                    val isDragged = dragState.draggedGroupId == group.id
                    val draggedIndex = dragState.draggedIndex
                    val hoveredIndex = dragState.hoveredIndex
                    val offset by animateFloatAsState(
                        when {
                            isDragged -> with(density) { dragState.draggedOffset.toDp() }.value
                            draggedIndex != null && hoveredIndex != null && draggedIndex >= hoveredIndex && group.index in hoveredIndex..draggedIndex -> 64f
                            draggedIndex != null && hoveredIndex != null && draggedIndex < hoveredIndex && group.index in draggedIndex..hoveredIndex -> (-64f)
                            else -> 0f
                        },
                        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
                    )


                    GroupItem(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .let {
                                if (!isDragged && dragState.draggedGroupId != null)
                                    it
                                        .height(64.dp)
                                        .offset(y = offset.dp)
                                else if (isDragged)
                                    it
                                        .offset(y = offset.dp)
                                        .zIndex(10f)
                                        .scale(1.025f)
                                        .shadow(8.dp)
                                else
                                    it
                            }
                            .let {
                                if (group.id == lastDraggedGroupId)
                                    it.zIndex(9f)
                                else
                                    it.animateItem()
                            },
                        group = group,
                        onEditClick = { onEvent(GroupsEvent.ShowEditGroupModal(group.id)) },
                        onDeleteClick = { onEvent(GroupsEvent.DeleteGroup(group.id)) }
                    )
                }
                item(
                    key = "add_button"
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .animateItem(),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        onClick = { onEvent(GroupsEvent.ShowAddGroupModal) },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.plus),
                            contentDescription = null
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Add Group")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun GroupsScreenPreview() {
    AppTheme {
        GroupsScreen(
            state = GroupsUiState(
                isLoading = false,
                groups = listOf(
                    Group(1, "Family"),
                    Group(2, "Friends"),
                    Group(3, "Work")
                )
            )
        )
    }
}

class GroupDragState(
    private val density: Density,
    private val innerPaddingTop: Dp,
    private val lazyListState: LazyListState,
) {
    var draggedGroupId by mutableStateOf<Int?>(null)
    var lastDraggedGroupId by mutableStateOf<Int?>(null)
    var startOffset by mutableFloatStateOf(0f)
    var draggedOffset by mutableFloatStateOf(0f)
    var draggedIndex by mutableStateOf<Int?>(null)
    var hoveredIndex by mutableStateOf<Int?>(null)

    fun onDragStart(offset: Offset, groups: List<Group>) {
        val id = getGroupAtOffset(offset, groups)
        if (id != null) {
            draggedGroupId = id
            startOffset = offset.y
            draggedIndex = groups.find { it.id == id }?.index
        }
    }

    fun onDrag(offset: Offset, groups: List<Group>) {
        draggedOffset = offset.y - startOffset
        val hoveredGroup = getGroupAtOffset(offset, groups).let { groupId ->
            groups.find { it.id == groupId }
        }

        hoveredIndex = hoveredGroup?.index ?: hoveredIndex
    }

    fun onDragEnd(onMove: (fromIndex: Int, toIndex: Int) -> Unit) {
        if (draggedIndex != null && hoveredIndex != null)
            onMove(draggedIndex!!, hoveredIndex!!)
        lastDraggedGroupId = draggedGroupId
        draggedGroupId = null
        draggedOffset = 0f
        startOffset = 0f
        hoveredIndex = null
        draggedIndex = null
    }

    fun onDragCancel() {
        draggedGroupId = null
        draggedOffset = 0f
        startOffset = 0f
        hoveredIndex = null
        draggedIndex = null
    }

    fun getGroupAtOffset(hitPoint: Offset, groups: List<Group>): Int? {
        return lazyListState.layoutInfo.visibleItemsInfo.find { itemInfo ->
            val hit = hitPoint.y - with(density) {
                (innerPaddingTop + 16.dp).roundToPx()
            }

            val itemStartAt = itemInfo.offset
            val itemEndAt = itemInfo.offset + itemInfo.size

            hit.roundToInt() in itemStartAt..itemEndAt
        }?.key as? Int
    }
}

@Composable
fun rememberGroupDragState(
    lazyListState: LazyListState,
    groups: List<Group>,
    innerPaddingTop: Dp,
): GroupDragState {
    val density = LocalDensity.current

    return remember(density, innerPaddingTop, lazyListState, groups) {
        GroupDragState(density, innerPaddingTop, lazyListState)
    }
}

fun Modifier.dragGroupHandler(
    dragState: GroupDragState,
    groups: List<Group>,
    onMove: (fromIndex: Int, toIndex: Int) -> Unit,
) = pointerInput(dragState, groups) {
    detectDragGesturesAfterLongPress(
        onDragStart = {
            dragState.onDragStart(it, groups)
        },
        onDragEnd = {
            dragState.onDragEnd(onMove)
        },
        onDragCancel = {
            dragState.onDragCancel()
        },
        onDrag = { change, _ ->
            dragState.onDrag(change.position, groups)
        },
    )
}
