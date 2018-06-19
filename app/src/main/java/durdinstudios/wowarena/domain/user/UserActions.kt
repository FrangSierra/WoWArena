package durdinstudios.wowarena.domain.user

import durdinstudios.wowarena.data.network.PlayerInfo
import durdinstudios.wowarena.data.network.PvpInfo
import durdinstudios.wowarena.misc.Task
import mini.Action

/**
 * Checks if user is already logged in.
 */
class CheckUserCredentialsAction : Action

/** Action dispatched when we know if we have credentials or not */
data class CheckUserCredentialsCompleteAction(val token: String?,
                                              val playerInfo: PlayerInfo?,
                                              val checkCredentialsTask: Task) : Action

/** Trigger when refresh token is completed. */
data class TokenRefreshedAction(val token: String?,
                                val customer: PlayerInfo?,
                                val refreshTask: Task) : Action