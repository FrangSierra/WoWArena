package durdinstudios.wowarena.ranking

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import durdinstudios.wowarena.R
import durdinstudios.wowarena.core.flux.NavigationFragment
import durdinstudios.wowarena.data.models.common.Region
import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.domain.user.ChangeCurrentRegionAction
import kotlinx.android.synthetic.main.ranking_fragment.*
import kotlinx.android.synthetic.main.toolbar.*
import mini.Dispatcher
import javax.inject.Inject

class RankingFragment : NavigationFragment() {

    @Inject
    lateinit var dispatcher: Dispatcher

    companion object {
        val TAG = "bracket_fragment"
        const val BRACKET = "bracket"
        fun newInstance(): RankingFragment {
            return RankingFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        return view ?: inflater.inflate(R.layout.ranking_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeInterface()
    }

    override fun onResume() {
        super.onResume()
        activity!!.toolbar.title = getString(R.string.navigation_ranking)
    }

    private fun initializeInterface() {
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.bracket_menu, menu)

        val item = menu.findItem(R.id.region_spinner)
        val spinner = item.actionView as Spinner

        val adapter = ArrayAdapter.createFromResource(activity,
                R.array.region_values, R.layout.spinner_custom_text)
        adapter.setDropDownViewResource(R.layout.custom_dropdown)

        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                dispatcher.dispatchOnUi(ChangeCurrentRegionAction(Region.values()[position]))
            }

        }
        super.onCreateOptionsMenu(menu, inflater)
    }
}
