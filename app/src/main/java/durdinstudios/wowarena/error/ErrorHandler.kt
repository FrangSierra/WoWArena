package durdinstudios.wowarena.error

import android.content.Context
import com.bq.masmov.reflux.dagger.AppScope
import dagger.Binds
import dagger.Module
import durdinstudios.wowarena.R
import durdinstudios.wowarena.domain.user.LowLevelCharacterException
import mini.Dispatcher
import mini.Grove
import org.json.JSONObject
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLPeerUnverifiedException

@Module
@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
interface ErrorHandlingModule {
    @Binds
    @AppScope
    fun provideErrorHandler(errorHandler: DefaultErrorHandler): ErrorHandler
}

/**
 * A basic application generated error with a message.
 */
class GenericError(message: String?, cause: Throwable?) : Exception(message, cause)

/**
 * Interface that exposes methods to handle app errors.
 */
interface ErrorHandler {
    /** Generate an user friendly message for known errors. */
    fun getMessageForError(e: Throwable?): String

    /** Handle the error, may result in a new activity being launched. */
    fun handle(e: Throwable?)

    /**
     * Unwrap an error into the underlying http code for manual handling,
     * or null if the error is not http related
     * */
    fun unwrapCode(e: Throwable?): Int?
}

/**
 * Class that implements the main handler of the application. It maps errors and exceptions to strings
 * or starts a new activity.
 */
class DefaultErrorHandler @Inject constructor(private val context: Context,
                                              private val dispatcher: Dispatcher) : ErrorHandler {

    @Suppress("UndocumentedPublicFunction")
    override fun getMessageForError(e: Throwable?): String {
        return when (e) {
            is HttpException -> {
                Grove.e { "${e.response().raw().request()}" }
                when {
                    e.response().errorBody() != null -> {
                        val body = JSONObject(e.response().errorBody()!!.string())
                        return body.getString("reason")
                    }
                    e.code() in 500..999 -> context.getString(R.string.error_internal, e.code())
                    else -> context.getString(R.string.error_unexpected)
                }
            }
            is LowLevelCharacterException -> context.getString(R.string.error_low_level)
            is GenericError -> e.message ?: context.getString(R.string.error_unexpected)
            is UnknownHostException -> context.getString(R.string.error_no_connection)
            is SSLPeerUnverifiedException -> context.getString(R.string.error_invalid_certificate)
            else -> {
                Grove.e { "Unexpected error: $e" }
                context.getString(R.string.error_unexpected)
            }
        }
    }

    @Suppress("UndocumentedPublicFunction")
    override fun handle(e: Throwable?) {
        val exception = e as? Exception ?: Exception(e)

        val errorCode = unwrapCode(e)
        if (errorCode == 401) {
            // Unauthorized
        }
    }

    @Suppress("UndocumentedPublicFunction")
    override fun unwrapCode(e: Throwable?): Int? {
        if (e is HttpException) return e.code()
        return null
    }
}

