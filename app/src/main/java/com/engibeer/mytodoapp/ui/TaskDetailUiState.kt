package com.engibeer.mytodoapp.ui

data class TaskDetailUiState(
    val mode: Mode,
    val btDeleteVisible: Boolean
)

enum class Mode {
    DEFAULT,
    CREATE,
    UPDATE_COMMON,
    UPDATE_DEADLINE_DATE,
    UPDATE_DEADLINE_TIME,
    SUCCESS_CREATE,
    SUCCESS_UPDATE,
    CONFIRM_DELETE,
    SUCCESS_DELETE,
    ERROR_VALIDATION
}