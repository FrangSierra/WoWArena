package durdinstudios.wowarena.domain.user

import com.bq.masmov.reflux.dagger.AppScope
import durdinstudios.wowarena.data.Service
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo
import durdinstudios.wowarena.data.network.WarcraftApi
import durdinstudios.wowarena.domain.user.token.TokenController
import durdinstudios.wowarena.misc.taskSuccess
import mini.Dispatcher
import javax.inject.Inject

/**
 * Controller that handles the user credentials and info.
 */
interface UserController {

    fun hasUserCredentials(userState: UserState)

    fun login(userId: String, password: String)

    @Suppress("UndocumentedPublicFunction")
    fun getPlayerInfoFromDisk(): PlayerInfo?

    @Suppress("UndocumentedPublicFunction")
    fun getToken(): String?

    fun persist(token: String? = null, playerInfo: PlayerInfo? = null)

    fun invalidateToken()
}

@AppScope
@Suppress("UndocumentedPublicClass")
class UserControllerImpl @Inject constructor(private val service: Service,
                                             private val dispatcher: Dispatcher,
                                             private val warcraftApi: WarcraftApi,
                                             private val tokenController: TokenController) : UserController {
    override fun hasUserCredentials(userState: UserState) {
        if (userState.token != null && userState.player != null) {
            dispatcher.dispatchOnUi(CheckUserCredentialsCompleteAction(userState.token, userState.player, taskSuccess()))
        } else {
            dispatcher.dispatchOnUi(CheckUserCredentialsCompleteAction(null, null, taskSuccess()))
        }
    }

    override fun login(userId: String, password: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPlayerInfoFromDisk(): PlayerInfo? =
        service.getPlayerInfoFromDisk()

    override fun getToken(): String? =
        tokenController.readTokenFromPersistence()

    override fun persist(token: String?, playerInfo: PlayerInfo?) {
        playerInfo?.run { service.savePlayerInfo(playerInfo) }
        token?.run { tokenController.updateTokenFromPersistence(token) }
    }

    override fun invalidateToken() {
        tokenController.removeTokenFromPersistence()
    }

}