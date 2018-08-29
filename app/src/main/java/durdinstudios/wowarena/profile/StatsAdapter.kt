package durdinstudios.wowarena.profile

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import durdinstudios.wowarena.R
import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.data.models.warcraft.pvp.BracketsPvp
import durdinstudios.wowarena.domain.arena.model.ArenaStats
import durdinstudios.wowarena.misc.colorCompat
import durdinstudios.wowarena.misc.colorify
import kotlinx.android.synthetic.main.profile_ranking_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class StatsAdapter : RecyclerView.Adapter<StatsAdapter.ViewHolder>() {
    val stats = mutableListOf<ArenaStats>()
    var currentBracket = ArenaBracket.BRACKET_3_VS_3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.profile_ranking_item, parent, false))

    override fun getItemCount() = stats.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = stats[position]
        val lastStats = stats.getOrNull(position - 1)
        val context = holder.itemView.context
        with(holder) {
            username.text = info.character.username
            info.character.klass.let { username.setTextColor(context.colorCompat(it.getClassColor())) }
            victories.text = info.getBracket(currentBracket)!!.seasonWon.toString()
            loses.text = info.getBracket(currentBracket)!!.seasonLost.toString()
            if (lastStats == null) {
                rating.text = info.getBracket(currentBracket)!!.rating.toString()
            } else {
                val newRating = info.getBracket(currentBracket)!!.rating
                val oldRating = lastStats.getBracket(currentBracket)!!.rating
                val difference = newRating - oldRating
                val castedText = if (difference < 0 || difference == 0) difference.toString() else "+$difference"
                val differenceText = "$newRating ($castedText)"
                val color = context.colorCompat(if (difference > 0 && difference != 0) R.color.color_positive else R.color.color_negative)
                rating.text = differenceText.colorify(newRating.toString().length + 2, differenceText.lastIndex, color)
            }
            timeAgo.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date(info.timestamp))
        }
    }

    fun updateStats(stats: List<ArenaStats>, bracket: ArenaBracket) {
        val forceUpdate = currentBracket != bracket
        currentBracket = bracket
        val sortedStats = stats.filter { it.getBracket(bracket) != null }.sortedBy { it.timestamp }
        val diffResult = DiffUtil.calculateDiff(StatsDiff(this.stats, sortedStats))
        this.stats.clear()
        this.stats.addAll(sortedStats)
        diffResult.dispatchUpdatesTo(this)
        if (forceUpdate) notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.username
        val victories: TextView = itemView.victories
        val loses: TextView = itemView.loses
        val rating: TextView = itemView.rating
        val timeAgo: TextView = itemView.time_ago
    }

    inner class StatsDiff(private val oldList: List<ArenaStats>,
                          private val newList: List<ArenaStats>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].timestamp == newList[newItemPosition].timestamp

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}