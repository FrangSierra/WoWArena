package durdinstudios.wowarena.data.network

/**
 * Exception that is thrown when a Lumiere response with an error.
 */
class DataServiceException(val networkDataSourceError: NetworkDataSourceError) : Throwable()


@Suppress("UndocumentedPublicClass")
sealed class NetworkDataSourceError

/**
 * Class that wraps no specified api error.
 */
class GenericError(val throwable: Throwable?) : NetworkDataSourceError()

/**
 * Class that wraps api error.
 */
class ApiError(private val code: String, val description: String?, val message: String?) : NetworkDataSourceError() {
    /**
     * Maps Battle net Error to type error.
     */
    fun toBattleNetError(): BattleNetError = BattleNetError.UNKNOWN_ERROR //TODO
}

/**
 * Battle net typed errors.
 */
enum class BattleNetError {
    UNKNOWN_ERROR
}