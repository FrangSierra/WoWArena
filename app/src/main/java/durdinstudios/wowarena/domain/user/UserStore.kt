package durdinstudios.wowarena.domain.user

import android.content.Context
import com.bq.masmov.reflux.dagger.AppScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import durdinstudios.wowarena.domain.arena.service.scheduleJob
import durdinstudios.wowarena.misc.taskRunning
import mini.Reducer
import mini.Store
import javax.inject.Inject

/**
 * Store that has the user info.
 */
@AppScope
class UserStore @Inject constructor(private val userController: UserController,
                                    private val context: Context) : Store<UserState>() {

    override fun initialState(): UserState {
        return UserState(selectedCharacter = userController.restoreSession(),
                currentCharacters = userController.getUsers(),
                settings = userController.getSettings(),
                tutorialShown = !userController.shouldShowTutorial())
    }

    init {
        if (userController.shouldSetupArenaJob()) scheduleJob(context)
    }

    @Reducer
    fun loadUser(action: LoadUserDataAction): UserState {
        if (state.loadUserTask.isRunning()) return state
        userController.getUserData(action.nick, action.realm, state.currentRegion)
        return state.copy(loadUserTask = taskRunning())
    }

    @Reducer
    fun loadUserComplete(action: LoadUserDataCompleteAction): UserState {
        if (!state.loadUserTask.isRunning()) return state
        if (action.task.isSuccessful()) {
            val playersInfo = state.playersInfo.plus((action.info!!.name to action.info.realm) to action.info)
            val characters = state.currentCharacters.plus(action.character!!).distinct()
            return state.copy(playersInfo = playersInfo, loadUserTask = action.task, currentCharacters = characters)
        }
        return state.copy(loadUserTask = action.task)
    }

    @Reducer
    fun deleteUser(action: DeleteUserAction): UserState {
        if (state.deleteTask.isRunning()) return state
        userController.deleteCharacter(action.character)
        return state.copy(deleteTask = taskRunning())
    }

    @Reducer
    fun userDeleted(action: DeleteUserCompleteAction): UserState {
        if (!state.deleteTask.isRunning()) return state
        val newChars =
                if (action.task.isSuccessful()) state.currentCharacters.filterNot {
                    it.characterEqualsTo(action.character.username, action.character.realm)
                }
                else state.currentCharacters
        return state.copy(deleteTask = taskRunning(), currentCharacters = newChars)
    }

    @Reducer
    fun set2vs2Settings(action: SetShow2vs2StatsSettingAction): UserState {
        userController.set2vs2StatsSettings(action.show)
        return state.copy(settings = state.settings.copy(show2vs2Stats = action.show))
    }

    @Reducer
    fun set3vs3Settings(action: SetShow3vs3StatsSettingAction): UserState {
        userController.set3vs3StatsSettings(action.show)
        return state.copy(settings = state.settings.copy(show3vs3Stats = action.show))
    }

    @Reducer
    fun setRbgSettings(action: SetShowRbgStatsSettingAction): UserState {
        userController.setRbgStatsSettings(action.show)
        return state.copy(settings = state.settings.copy(showRbgStats = action.show))
    }

}

@Module
interface UserModule {
    @Binds
    @AppScope
    @IntoMap
    @ClassKey(UserStore::class)
    fun storeToMap(store: UserStore): Store<*>

    @Binds
    @AppScope
    fun userController(userControllerImpl: UserControllerImpl): UserController
}