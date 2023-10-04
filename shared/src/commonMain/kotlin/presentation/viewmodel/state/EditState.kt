package presentation.viewmodel.state

sealed class EditState {
    object Loading : EditState()
    object Initial : EditState()
    data class Error(val message: String) : EditState()
    object Success : EditState()
    object Delete : EditState()
}
