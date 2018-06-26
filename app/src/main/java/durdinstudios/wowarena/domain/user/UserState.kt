package durdinstudios.wowarena.domain.user

import durdinstudios.wowarena.data.models.common.Region
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo
import durdinstudios.wowarena.misc.Task
import durdinstudios.wowarena.misc.taskIdle

@Suppress("UndocumentedPublicClass")
data class UserState(
        val player: PlayerInfo? = null,
        val currentRegion: Region = Region.EU,
        val loadUserTask: Task = taskIdle()) {

    /**
     * Checks if user has local credentials.
     */
    val isLoggedIn get() = player != null

}