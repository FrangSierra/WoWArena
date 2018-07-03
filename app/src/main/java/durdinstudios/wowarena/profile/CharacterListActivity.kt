package durdinstudios.wowarena.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import durdinstudios.wowarena.R
import durdinstudios.wowarena.core.dagger.BaseActivity
import durdinstudios.wowarena.domain.user.DeleteUserAction
import durdinstudios.wowarena.domain.user.UserStore
import durdinstudios.wowarena.error.ErrorHandler
import durdinstudios.wowarena.misc.GridSpacingItemDecoration
import durdinstudios.wowarena.misc.filterOne
import durdinstudios.wowarena.misc.setGridLayoutManager
import durdinstudios.wowarena.misc.toast
import durdinstudios.wowarena.navigation.HomeActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.character_list_activity.*
import kotlinx.android.synthetic.main.toolbar.*
import mini.Dispatcher
import mini.select
import javax.inject.Inject


class CharacterListActivity : BaseActivity() {

    @Inject
    lateinit var dispatcher: Dispatcher
    @Inject
    lateinit var userStore: UserStore
    @Inject
    lateinit var errorHandler: ErrorHandler

    private val adapter = CharacterAdapter(::onCharacterClick)

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, CharacterListActivity::class.java)
    }

    override fun onResume() {
        super.onResume()
        toolbar.title = getString(R.string.navigation_characters)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.order) {
            CharacterAdapter.DELETE_MENU_ID -> {
                onDeleteCharacterClick(adapter.getCharacter(item.groupId))
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_list_activity)
        initializeInterface()
        listenStoreChanges()
    }

    private fun listenStoreChanges() {
        userStore.flowable()
                .select { it.currentCharacters }
                .subscribe { adapter.updateCharacters(it) }
                .track()
    }

    private fun initializeInterface() {
        add_char.setOnClickListener { addNewCharacter() }
        players_recycler.setGridLayoutManager(this, 2, reverseLayout = false, stackFromEnd = false)
        players_recycler.addItemDecoration(GridSpacingItemDecoration(2, 32, true))
        players_recycler.adapter = adapter
        registerForContextMenu(players_recycler)
    }

    private fun addNewCharacter() {
        startActivity(AddCharacterActivity.newIntent(this))
    }

    private fun onDeleteCharacterClick(character: Character) {
        dispatcher.dispatchOnUi(DeleteUserAction(character))
        userStore.flowable()
                .observeOn(AndroidSchedulers.mainThread())
                .select { it.deleteTask }
                .filterOne { it.isTerminal() }
                .subscribe {
                    if (it.isFailure()) {
                        toast(errorHandler.getMessageForError(it.error!!))
                    }
                }.track()
    }

    private fun onCharacterClick(info: Character) {
        startActivity(HomeActivity.newIntent(this, name = info.username, realm = info.realm, region = info.region))
    }
}