package durdinstudios.wowarena.core.flux

import android.support.v7.widget.Toolbar
import durdinstudios.wowarena.R
import durdinstudios.wowarena.core.dagger.BaseFragment
import durdinstudios.wowarena.misc.drawableCompat

abstract class NavigationFragment : BaseFragment(){
    var toolbar: Toolbar? = null

    override fun onResume() {
        super.onResume()
        if (activity == null) return
        toolbar = activity?.findViewById(R.id.toolbar)
        toolbar?.navigationIcon =
                if (activity!!.supportFragmentManager.backStackEntryCount > 0)
                    activity!!.drawableCompat(R.drawable.ic_arrow_back)
                else null
        toolbar?.setNavigationOnClickListener { activity?.onBackPressed() }
    }
}