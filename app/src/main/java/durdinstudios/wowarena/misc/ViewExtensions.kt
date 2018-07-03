package durdinstudios.wowarena.misc

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.EditText

fun RecyclerView.setLinearLayoutManager(context: Context, reverseLayout: Boolean = true, stackFromEnd: Boolean = true) {
    val linearLayoutManager = LinearLayoutManager(context)
    linearLayoutManager.reverseLayout = reverseLayout
    linearLayoutManager.stackFromEnd = stackFromEnd
    layoutManager = linearLayoutManager
}

fun RecyclerView.setGridLayoutManager(context: Context, columns : Int,
                                      reverseLayout: Boolean = true, stackFromEnd: Boolean = true){
    val gridLayoutManager = GridLayoutManager(context, columns)
    gridLayoutManager.reverseLayout = reverseLayout
    gridLayoutManager.stackFromEnd = stackFromEnd
    layoutManager = gridLayoutManager
}