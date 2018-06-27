package durdinstudios.wowarena.profile

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import durdinstudios.wowarena.R
import durdinstudios.wowarena.data.models.common.Region
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo
import durdinstudios.wowarena.data.models.warcraft.pvp.getRenderUrl
import durdinstudios.wowarena.misc.colorCompat
import durdinstudios.wowarena.misc.setCircularImage
import kotlinx.android.synthetic.main.player_item.view.*


class CharacterAdapter(private val onPlayerclick: (info: Character) -> Unit) : RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {
    private val characters: MutableList<Character> = ArrayList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.player_item, viewGroup, false))

    override fun getItemCount() = characters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = characters[position]
        val context = holder.itemView.context
        with(holder) {
            avatar.setCircularImage(info.getRenderUrl(Region.EU)) //TODO
            nick.text = info.username
            nick.setTextColor(context.colorCompat(info.klass.getClassColor()))
            data.text = "${info.level} ${info.race.name} ${info.klass.name}"
            realm.text = info.realm
            itemView.setOnClickListener { onPlayerclick(info) }
        }
    }

    fun updateCharacters(rankingList: List<Character>) {
        val newSortedUsers = rankingList
        val diffResult = DiffUtil.calculateDiff(CharacterDiff(this.characters, newSortedUsers))
        this.characters.clear()
        this.characters.addAll(newSortedUsers)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView = itemView.avatar
        val nick: TextView = itemView.username
        val data: TextView = itemView.character_data
        val realm: TextView = itemView.realm
    }

    inner class CharacterDiff(private val oldList: List<Character>,
                              private val newList: List<Character>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldList[oldItemPosition].username == newList[newItemPosition].username
                        && oldList[oldItemPosition].realm == newList[newItemPosition].realm

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldList[oldItemPosition] == newList[newItemPosition]
    }
}