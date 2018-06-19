package durdinstudios.wowarena.misc

/** State of the task. Idle is not a terminal state*/
enum class TaskStatus {
    IDLE,
    RUNNING,
    SUCCESS,
    ERROR
}

/**
 * Basic object to represent an ongoing process.
 */
data class TypedTask<out T>(val status: TaskStatus = TaskStatus.IDLE,
                            val metadata: T,
                            val error: Throwable? = null) {

    @Suppress("UndocumentedPublicFunction")
    fun isRunning() = status == TaskStatus.RUNNING

    @Suppress("UndocumentedPublicFunction")
    fun isFailure() = status == TaskStatus.ERROR

    @Suppress("UndocumentedPublicFunction")
    fun isTerminal(): Boolean = status == TaskStatus.SUCCESS || status == TaskStatus.ERROR

    @Suppress("UndocumentedPublicFunction")
    fun isSuccessful() = status == TaskStatus.SUCCESS
}

typealias Task = TypedTask<Nothing?>

//Factory functions

/** Idle task **/
fun <T> taskIdle(data: T): TypedTask<T> = TypedTask(TaskStatus.IDLE, data, null)

/** Sets the task as succeeded with data. */
fun <T> taskSuccess(data: T): TypedTask<T> = TypedTask(TaskStatus.SUCCESS, data, null)

/** Sets the task as running. */
fun <T> taskRunning(data: T): TypedTask<T> = TypedTask(TaskStatus.RUNNING, data, null)

/** Sets the task as error, with its cause. */
fun <T> taskFailure(data: T, e: Throwable? = null): TypedTask<T> = TypedTask(TaskStatus.ERROR, data, e)

//Factory functions for nullable types

/** Idle task **/
fun <T> taskIdle(): TypedTask<T?> = TypedTask(TaskStatus.IDLE, null, null)

/** Tasks success for nullable types */
fun <T> taskSuccess(): TypedTask<T?> = TypedTask(TaskStatus.SUCCESS, null, null)

/** Tasks running for nullable types */
fun <T> taskRunning(): TypedTask<T?> = TypedTask(TaskStatus.RUNNING, null, null)

/** Tasks error for nullable types */
fun <T> taskFailure(error: Throwable? = null): TypedTask<T?> = TypedTask(TaskStatus.ERROR, null, error)

