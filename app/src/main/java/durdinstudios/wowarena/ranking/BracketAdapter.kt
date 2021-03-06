package durdinstudios.wowarena.ranking

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import durdinstudios.wowarena.R
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerBracketStats
import durdinstudios.wowarena.misc.QueueAdapter
import durdinstudios.wowarena.misc.colorCompat
import durdinstudios.wowarena.misc.setImage
import kotlinx.android.synthetic.main.ranking_item.view.*


class BracketAdapter(private val onPlayerclick: (stats: PlayerBracketStats) -> Unit) : QueueAdapter<PlayerBracketStats, BracketAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.ranking_item, viewGroup, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val statsItem = items[position]
        val context = holder.itemView.context
        with(holder) {
            this.itemView.setOnClickListener { onPlayerclick(statsItem) }
            this.position.text = statsItem.ranking.toString()
            player.text = statsItem.name
            statsItem.wowClass?.let { player.setTextColor(context.colorCompat(it.getClassColor())) }
            //realm.text = statsItem.realmName
            faction.setImage(statsItem.faction.getFactionIcon())
            faction.setColorFilter(context.colorCompat(statsItem.faction.getFactionTint()))
            victories.text = statsItem.seasonWins.toString()
            loses.text = statsItem.seasonLosses.toString()
            rating.text = statsItem.rating.toString()
        }
    }


    fun updateRanking(bracketInfo: List<PlayerBracketStats>) {
        val sortItems = bracketInfo.sortedBy { it.ranking }
        updateItems(RankingDiff(items, sortItems), sortItems)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val position: TextView = itemView.position
        val player: TextView = itemView.player_name
        //val realm: TextView = itemView.realm
        val faction: ImageView = itemView.faction
        val victories: TextView = itemView.victories
        val loses: TextView = itemView.loses
        val rating: TextView = itemView.rating
    }

    inner class RankingDiff(private val oldList: List<PlayerBracketStats>,
                            private val newList: List<PlayerBracketStats>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldList[oldItemPosition].name == newList[newItemPosition].name
                        && oldList[oldItemPosition].realmId == newList[newItemPosition].realmId

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldList[oldItemPosition] == newList[newItemPosition]
    }
}