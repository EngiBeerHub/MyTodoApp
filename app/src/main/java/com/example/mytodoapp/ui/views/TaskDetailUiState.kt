package com.example.mytodoapp.ui.views

data class TaskDetailUiState(
    val mode: Mode,
    val btDeleteVisible: Boolean
)

enum class Mode {
    DEFAULT,
    CREATE,
    UPDATE_COMMON,
    UPDATE_DEADLINE,
    SUCCESS_CREATE,
    SUCCESS_UPDATE,
    CONFIRM_DELETE,
    SUCCESS_DELETE,
    ERROR_VALIDATION
}