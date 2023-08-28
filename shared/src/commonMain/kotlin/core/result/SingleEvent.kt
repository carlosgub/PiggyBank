package core.result

open class SingleEvent<out T>(private val content: T) {
    var hasBeenHandled = false
        private set

    fun get(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}
