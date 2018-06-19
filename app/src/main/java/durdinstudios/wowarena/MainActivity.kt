package durdinstudios.wowarena

import android.os.Bundle
import durdinstudios.wowarena.core.dagger.BaseActivity
import durdinstudios.wowarena.data.Service
import durdinstudios.wowarena.data.models.ArenaBracket
import durdinstudios.wowarena.data.network.LeaderboardBody
import durdinstudios.wowarena.data.network.WarcraftApi
import durdinstudios.wowarena.domain.leaderboard.LeaderboardStore
import mini.Dispatcher
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var dispatcher: Dispatcher
    @Inject
    lateinit var leaderboardStore: LeaderboardStore
    @Inject
    lateinit var api: Service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val player = api.getPvpLeaderboard(ArenaBracket.BRACKET_2_VS_2)
        print(player)
    }
}
