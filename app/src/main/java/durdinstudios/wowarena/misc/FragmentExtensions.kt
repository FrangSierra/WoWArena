package durdinstudios.wowarena.misc

import android.support.v4.app.FragmentTransaction
import durdinstudios.wowarena.R

fun FragmentTransaction.withFade(): FragmentTransaction {
    return setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
}