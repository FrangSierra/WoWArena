package durdinstudios.wowarena.misc

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


abstract class QueueAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    protected var items: MutableList<T> = ArrayList()
    private val pendingUpdates: Queue<List<T>> = ArrayDeque()

    fun updateItems(diff: DiffUtil.Callback, newItems: List<T>) {
        pendingUpdates.add(newItems)
        if (pendingUpdates.size > 1) {
            return
        }
        updateItemsInternal(diff, newItems)
    }

    private fun applyDiffResult(diff: DiffUtil.Callback,
                                newItems: List<T>,
                                diffResult: DiffUtil.DiffResult) {
        pendingUpdates.remove()
        dispatchUpdates(newItems, diffResult)
        if (pendingUpdates.size > 0) {
            updateItemsInternal(diff, pendingUpdates.peek())
        }
    }

    // This method does the heavy lifting of
    // pushing the work to the background thread
    private fun updateItemsInternal(diff: DiffUtil.Callback, newItems: List<T>) {
        Single.create<DiffUtil.DiffResult> { it.onSuccess(DiffUtil.calculateDiff(diff)) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ applyDiffResult(diff, newItems, it) }, { TODO() })
    }

    // This method does the work of actually updating
    // the backing data and notifying the adapter
    private fun dispatchUpdates(newItems: List<T>,
                                diffResult: DiffUtil.DiffResult) {
        diffResult.dispatchUpdatesTo(this)
        items.clear()
        items.addAll(newItems)
    }

}