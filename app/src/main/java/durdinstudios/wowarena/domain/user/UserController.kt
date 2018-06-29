package durdinstudios.wowarena.domain.user

import com.bq.masmov.reflux.dagger.AppScope
import durdinstudios.wowarena.data.WarcraftAPIInstances
import durdinstudios.wowarena.data.models.common.Region
import durdinstudios.wowarena.misc.taskFailure
import durdinstudios.wowarena.misc.taskSuccess
import durdinstudios.wowarena.profile.Character
import durdinstudios.wowarena.profile.toCharacter
import io.reactivex.schedulers.Schedulers
import mini.Dispatcher
import javax.inject.Inject

/**
 * Controller that handles the user credentials and info.
 */
interface UserController {

    fun getUserData(username: String, realm: String, region: Region)

    fun saveSession(user: Character)

    fun restoreSession(): Character?

    fun getUsers(): List<Character>

    fun deleteCharacter(user: Character)

    fun shouldSetupArenaJob(): Boolean
}

class LowLevelCharacterException : Throwable()

@AppScope
class UserControllerImpl @Inject constructor(private val dispatcher: Dispatcher,
                                             private val userRepository: UserRepository,
                                             private val warcraftApi: WarcraftAPIInstances) : UserController {
    companion object {
        const val MAX_LEVEL = 110
    }

    override fun getUserData(username: String, realm: String, region: Region) {
        warcraftApi.apis[region]!!.getPlayerPvpInfo(username, realm)
                .subscribeOn(Schedulers.io())
                .subscribe({ user ->
                    if (user.level < MAX_LEVEL) {
                        dispatcher.dispatchOnUi(LoadUserDataCompleteAction(null, null, taskFailure(LowLevelCharacterException())))
                    } else {
                        userRepository.saveUser(user.toCharacter(region))
                        dispatcher.dispatchOnUi(LoadUserDataCompleteAction(user, user.toCharacter(region), taskSuccess()))
                    }
                }, { error ->
                    dispatcher.dispatchOnUi(LoadUserDataCompleteAction(null, null, taskFailure(error)))
                })
    }

    override fun saveSession(user: Character) {
        userRepository.saveUser(user)
    }

    override fun restoreSession(): Character? {
        return userRepository.getCurrentUser()
    }

    override fun getUsers(): List<Character> {
        return userRepository.getUsers()
    }

    override fun deleteCharacter(user: Character) {
        try {
            userRepository.removeUser(user)
            dispatcher.dispatchOnUi(DeleteUserCompleteAction(user, taskSuccess()))
        } catch (e : Exception){
            dispatcher.dispatchOnUi(DeleteUserCompleteAction(user, taskFailure(e)))
        }
    }

    override fun shouldSetupArenaJob(): Boolean {
        return userRepository.shouldSetupArenaJob()
    }
}