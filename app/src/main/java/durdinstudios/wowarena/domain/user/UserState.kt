package durdinstudios.wowarena.domain.user

import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo
import durdinstudios.wowarena.misc.Task
import durdinstudios.wowarena.misc.taskIdle

@Suppress("UndocumentedPublicClass")
data class UserState(
    val player: PlayerInfo? = null,
    val token: String? = null,
    val loginTask: Task = taskIdle(),
    val checkCredentialsTask: Task = taskIdle(),
    val refreshTaskState: Task = taskIdle()) {

    /**
     * Checks if user has local credentials.
     */
    val isLoggedIn get() = token != null && player != null

}