package durdinstudios.wowarena.core.dagger

import android.annotation.SuppressLint
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.toolbar.*
import mini.DefaultSubscriptionTracker
import mini.SubscriptionTracker

/** Base [Activity] to use with Flux+Dagger in the app. */
@SuppressLint("Registered")
abstract class BaseActivity :
        DaggerAppCompatActivity(),
        SubscriptionTracker by DefaultSubscriptionTracker() {

    override fun onResume() {
        super.onResume()
        toolbar?.let { setSupportActionBar(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelSubscriptions()
    }
}