package com.elfennani.ledgerly.presentation.scene.groups

sealed class GroupsEvent {
    // Modals
    data object ShowAddGroupModal : GroupsEvent()
    data object DismissAddGroupModal : GroupsEvent()
    data class ShowEditGroupModal(val groupId: Int) : GroupsEvent()
    data object DismissEditGroupModal : GroupsEvent()

    // Form Events
    data class OnGroupNameChanged(val name: String) : GroupsEvent()
    data object SubmitAddGroupForm : GroupsEvent()
    data class SubmitEditGroupForm(val groupId: Int) : GroupsEvent()

    // Group Actions
    data class DeleteGroup(val groupId: Int) : GroupsEvent()
}