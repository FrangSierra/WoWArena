package durdinstudios.wowarena.domain.user

import com.bq.masmov.reflux.dagger.AppScope
import durdinstudios.wowarena.data.WarcraftAPIInstances
import durdinstudios.wowarena.data.models.common.Region
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo
import durdinstudios.wowarena.misc.taskFailure
import durdinstudios.wowarena.misc.taskSuccess
import io.reactivex.schedulers.Schedulers
import mini.Dispatcher
import javax.inject.Inject

/**
 * Controller that handles the user credentials and info.
 */
interface UserController {

    fun getUserData(username: String, realm: String, region: Region)

    fun saveSession(user: PlayerInfo)

    fun restoreSession(): PlayerInfo?

    fun getUsers(): List<PlayerInfo>

    fun logout(user: PlayerInfo)
}

@AppScope
class UserControllerImpl @Inject constructor(private val dispatcher: Dispatcher,
                                             private val userRepository: UserRepository,
                                             private val warcraftApi: WarcraftAPIInstances) : UserController {

    override fun getUserData(username: String, realm: String, region: Region) {
        warcraftApi.apis[region]!!.getPlayerPvpInfo(username, realm)
                .subscribeOn(Schedulers.io())
                .subscribe({ user ->
                    userRepository.saveUser(user)
                    dispatcher.dispatchOnUi(LoadUserDataCompleteAction(user, taskSuccess()))
                }, { error ->
                    dispatcher.dispatchOnUi(LoadUserDataCompleteAction(null, taskFailure(error)))
                })
    }

    override fun saveSession(user: PlayerInfo) {
        userRepository.saveUser(user)
    }

    override fun restoreSession(): PlayerInfo? {
        return userRepository.getCurrentUser()
    }

    override fun getUsers(): List<PlayerInfo> {
        return userRepository.getUsers()
    }

    override fun logout(user: PlayerInfo) {
        userRepository.removeUser(user)
    }

}