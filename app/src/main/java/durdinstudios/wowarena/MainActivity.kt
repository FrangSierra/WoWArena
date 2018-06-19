package durdinstudios.wowarena

import android.os.Bundle
import durdinstudios.wowarena.core.dagger.BaseActivity
import durdinstudios.wowarena.data.models.common.Locale
import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo
import durdinstudios.wowarena.data.network.WarcraftApi
import durdinstudios.wowarena.domain.leaderboard.LeaderboardStore
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import mini.Dispatcher
import mini.Grove
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var dispatcher: Dispatcher
    @Inject
    lateinit var leaderboardStore: LeaderboardStore
    @Inject
    lateinit var api: WarcraftApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bracket = ArenaBracket.BRACKET_2_VS_2.value
        Single.create<PlayerInfo> {
            try {
                val execute = api.getPlayerPvpInfo("Soulex", "Sanguino", locale = Locale.SPANISH.value).execute()
                it.onSuccess(execute.body()!!)
            } catch (e: Exception) {
                it.onError(e)
            }
        }.subscribeOn(Schedulers.io())
            .subscribe({
                    Grove.i { it }
            }, {
                Grove.e { it }
            })
    }
}
