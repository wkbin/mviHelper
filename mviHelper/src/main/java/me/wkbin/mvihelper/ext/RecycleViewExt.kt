package me.wkbin.mvihelper.ext

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.wkbin.mvihelper.util.DefaultDivider


fun RecyclerView.vertical():RecyclerView{
    layoutManager = LinearLayoutManager(context)
    setHasFixedSize(true)
    return this
}

fun RecyclerView.horizontal(): RecyclerView {
    layoutManager = LinearLayoutManager(context).apply {
        orientation = RecyclerView.HORIZONTAL
    }
    setHasFixedSize(true)
    return this
}

fun RecyclerView.divider(block:DefaultDivider.() -> Unit):RecyclerView{
    val itemDecoration = DefaultDivider(context)
    block.invoke(itemDecoration)
    addItemDecoration(itemDecoration)
    return this
}