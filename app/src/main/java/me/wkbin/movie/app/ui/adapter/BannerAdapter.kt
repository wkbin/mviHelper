package me.wkbin.movie.app.ui.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder
import me.wkbin.movie.R
import me.wkbin.movie.app.GlideApp
import javax.inject.Inject

class BannerAdapter @Inject constructor():BaseBannerAdapter<String>() {
    override fun bindData(
        holder: BaseViewHolder<String>?,
        data: String?,
        position: Int,
        pageSize: Int
    ) {
        holder?:return
        data?:return
        val imageView = holder.itemView.findViewById<ImageView>(R.id.banner_image)
        GlideApp.with(holder.itemView.context).load(data).into(imageView)
    }

    override fun getLayoutId(viewType: Int) = R.layout.item_banner
}