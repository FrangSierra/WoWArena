package durdinstudios.wowarena.profile

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import durdinstudios.wowarena.R
import durdinstudios.wowarena.data.models.common.Region
import durdinstudios.wowarena.data.models.warcraft.pvp.getRenderUrl
import durdinstudios.wowarena.misc.colorCompat
import durdinstudios.wowarena.misc.setCircularImage
import kotlinx.android.synthetic.main.character_item.view.*

class CharacterAdapter(private val onPlayerClick: (info: Character) -> Unit) : RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {
    private val characters: MutableList<Character> = ArrayList()

    companion object {
        const val DELETE_MENU_ID = 101
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.character_item, viewGroup, false))

    override fun getItemCount() = characters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = characters[position]
        val context = holder.itemView.context
        with(holder) {
            avatar.setCircularImage(info.getRenderUrl(Region.EU)) //TODO
            nick.text = info.username
            nick.setTextColor(context.colorCompat(info.klass.getClassColor()))
            realm.text = info.realm
            itemView.setOnClickListener { onPlayerClick(info) }
        }
    }

    fun updateCharacters(rankingList: List<Character>) {
        val newSortedUsers = rankingList
        val diffResult = DiffUtil.calculateDiff(CharacterDiff(this.characters, newSortedUsers))
        this.characters.clear()
        this.characters.addAll(newSortedUsers)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getCharacter(position: Int) = characters[position]

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
        override fun onCreateContextMenu(menu: ContextMenu?, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
            menu?.add(adapterPosition, v.id, DELETE_MENU_ID, itemView.context.getString(R.string.character_adapter_menu_delete))
        }

        val avatar: ImageView = itemView.avatar
        val nick: TextView = itemView.username
        val realm: TextView = itemView.realm

        init {
            itemView.setOnCreateContextMenuListener(this)
        }
    }


    inner class CharacterDiff(private val oldList: List<Character>,
                              private val newList: List<Character>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldList[oldItemPosition]
                        .characterEqualsTo(newList[newItemPosition].username, newList[newItemPosition].realm)

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldList[oldItemPosition] == newList[newItemPosition]
    }
}