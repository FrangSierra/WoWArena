package durdinstudios.wowarena.ranking

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import durdinstudios.wowarena.R
import durdinstudios.wowarena.core.dagger.BaseFragment
import durdinstudios.wowarena.core.flux.NavigationFragment
import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerBracketStats
import durdinstudios.wowarena.domain.leaderboard.LeaderboardStore
import durdinstudios.wowarena.domain.leaderboard.LoadLeaderboardAction
import durdinstudios.wowarena.misc.argument
import durdinstudios.wowarena.misc.filterOne
import durdinstudios.wowarena.misc.setLinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.bracket_fragment.*
import kotlinx.android.synthetic.main.ranking_fragment.*
import kotlinx.android.synthetic.main.toolbar.*
import mini.Dispatcher
import mini.select
import javax.inject.Inject

class RankingFragment : NavigationFragment() {

    companion object {
        val TAG = "bracket_fragment"
        const val BRACKET = "bracket"
        fun newInstance(): RankingFragment {
            return RankingFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = view
            ?: inflater.inflate(R.layout.ranking_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeInterface()
    }

    override fun onResume() {
        super.onResume()
        activity!!.toolbar.title = getString(R.string.navigation_ranking)
    }
    
    private fun initializeInterface(){
        ranking_viewpager.adapter = RankingAdapter(childFragmentManager)
        ranking_viewpager.currentItem = 1
        ranking_viewpager.offscreenPageLimit = 0
        ranking_tab_layout.setupWithViewPager(ranking_viewpager)
    }

    inner class RankingAdapter(supportFragmentManager: FragmentManager) : FragmentPagerAdapter(supportFragmentManager) {
        override fun getItem(position: Int): Fragment = when (position) {
            0 -> BracketFragment.newInstance(ArenaBracket.BRACKET_2_VS_2)
            1 -> BracketFragment.newInstance(ArenaBracket.BRACKET_3_VS_3)
            2 -> BracketFragment.newInstance(ArenaBracket.RBG)
            else -> throw IllegalAccessError("No Fragment for the given position")
        }

        override fun getCount(): Int = 3

        override fun getPageTitle(position: Int): CharSequence =
                when (position) {
                    0 -> getString(R.string.bracket_2vs2)
                    1 -> getString(R.string.bracket_3vs3)
                    2 -> getString(R.string.bracket_rbg)
                    else -> throw IllegalAccessError("No Fragment for the given position")
                }
    }
}
