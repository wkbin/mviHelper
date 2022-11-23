package me.wkbin.movie.app.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import me.wkbin.movie.R
import me.wkbin.movie.app.data.HomeData
import javax.inject.Inject

class MovieAdapter @Inject constructor():BaseQuickAdapter<HomeData,QuickViewHolder>() {

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: HomeData?) {
        holder.setText(R.id.tvMoviceName,item?.name)
        Glide.with(holder.itemView.context).load(item?.pic).into(holder.getView(R.id.ivMovieIcon) as ImageView)
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_movie,parent)
    }
}