package presentation.viewmodel.state

sealed class EditSideEffects {
    object Loading : EditSideEffects()
    object Initial : EditSideEffects()
    data class Error(val message: String) : EditSideEffects()
    object Success : EditSideEffects()
    object Delete : EditSideEffects()
}
