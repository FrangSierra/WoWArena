package durdinstudios.wowarena.navigation

import android.content.Context
import durdinstudios.wowarena.R
import durdinstudios.wowarena.core.flux.NavigationFragment
import durdinstudios.wowarena.profile.ProfileFragment
import durdinstudios.wowarena.ranking.RankingFragment
import durdinstudios.wowarena.settings.SettingsFragment

class NavigationAdapter {

    private val fragmentMap = mutableMapOf<Int, NavigationFragment>()

    fun getItem(position: Int) = when (position) {
        0 -> fragmentMap.getOrPut(position) { RankingFragment.newInstance() }
        1 -> fragmentMap.getOrPut(position) { ProfileFragment.newInstance("Soulex", "Sanguino") }
        2 -> fragmentMap.getOrPut(position) { SettingsFragment.newInstance() }
        else -> throw IllegalAccessError("No Fragment for the given position")
    }

    fun getItemTitle(context: Context, position: Int) =
            when (position) {
                0 -> context.getString(R.string.navigation_ranking)
                1 -> context.getString(R.string.navigation_profile)
                2 -> context.getString(R.string.navigation_settings)
                else -> throw IllegalAccessError("No Fragment for the given position")
            }

    fun hasInstance(position: Int): Boolean {
        return fragmentMap[position] != null
    }
}