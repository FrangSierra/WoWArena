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
import durdinstudios.wowarena.misc.colorCompat
import durdinstudios.wowarena.misc.drawableCompat
import kotlinx.android.synthetic.main.ranking_item.view.*

class BracketAdapter(private val onPlayerclick: (stats: PlayerBracketStats) -> Unit) : RecyclerView.Adapter<BracketAdapter.ViewHolder>() {
    private val rankingList: MutableList<PlayerBracketStats> = ArrayList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.ranking_item, viewGroup, false))

    override fun getItemCount() = rankingList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val statsItem = rankingList[position]
        val context = holder.itemView.context
        with(holder) {
            this.position.text = statsItem.ranking.toString()
            player.text = statsItem.name
            statsItem.wowClass?.let { player.setTextColor(context.colorCompat(it.getClassColor())) }
            //realm.text = statsItem.realmName
            faction.setImageDrawable(context.drawableCompat(statsItem.faction.getFactionIcon()))
            faction.setColorFilter(context.colorCompat(statsItem.faction.getFactionTint()))
            victories.text = statsItem.seasonWins.toString()
            loses.text = statsItem.seasonLosses.toString()
            rating.text = statsItem.rating.toString()
        }
    }

    fun updateRanking(rankingList: List<PlayerBracketStats>) {
        val newSortedUsers = rankingList.sortedBy { it.ranking }
        val diffResult = DiffUtil.calculateDiff(RankingDiff(this.rankingList, newSortedUsers))
        this.rankingList.clear()
        this.rankingList.addAll(newSortedUsers)
        diffResult.dispatchUpdatesTo(this)
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