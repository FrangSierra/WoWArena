package durdinstudios.wowarena.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import durdinstudios.wowarena.R
import durdinstudios.wowarena.core.dagger.BaseActivity
import durdinstudios.wowarena.data.models.common.Region
import durdinstudios.wowarena.domain.arena.ArenaStore
import durdinstudios.wowarena.domain.user.LoadUserDataAction
import durdinstudios.wowarena.domain.user.UserStore
import durdinstudios.wowarena.error.ErrorHandler
import durdinstudios.wowarena.misc.filterOne
import durdinstudios.wowarena.misc.hideKeyboard
import durdinstudios.wowarena.misc.toast
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.add_character_activity.*
import mini.Dispatcher
import mini.select
import javax.inject.Inject


class AddCharacterActivity : BaseActivity() {

    @Inject
    lateinit var dispatcher: Dispatcher
    @Inject
    lateinit var userStore: UserStore
    @Inject
    lateinit var errorHandler: ErrorHandler

    private val regions = Region.values().toList()

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, AddCharacterActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.add_character_activity)
        initializeInterface()
    }

    private fun initializeInterface() {
        nice_spinner.attachDataSource(regions.map { it.name.capitalize() })
        add_user.setOnClickListener { addCharacter() }
    }

    private fun addCharacter() {
        hideKeyboard()
        add_user.isEnabled = false
        dispatcher.dispatchOnUi(LoadUserDataAction(username.text.toString(), realm.text.toString(), regions[nice_spinner.selectedIndex]))
        userStore.flowable()
                .observeOn(AndroidSchedulers.mainThread())
                .select { it.loadUserTask }
                .filterOne { it.isTerminal() }
                .subscribe {
                    if (it.isFailure()) {
                        toast(errorHandler.getMessageForError(it.error!!))
                    } else {
                        finish()
                    }
                    add_user.isEnabled = true
                }.track()
    }
}