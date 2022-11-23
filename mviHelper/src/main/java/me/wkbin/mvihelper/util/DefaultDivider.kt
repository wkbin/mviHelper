package me.wkbin.mvihelper.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class DefaultDivider(val context:Context):RecyclerView.ItemDecoration() {
    companion object{
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
        const val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL
        const val VERTICAL_LIST = LinearLayoutManager.VERTICAL
    }

    // 分割线绘制所需要的Drawable , 当然也可以直接使用 Canvas 绘制，只不过我这里使用 Drawable
    private var mDivider:Drawable = ColorDrawable(Color.parseColor("#00000000"))
    // 列表方向
    var mOrientation:Int = VERTICAL_LIST
    // 分割线宽度或高度
    var mDividerSize:Int = 0
    // 分割线距离两端的间距
    var mIncludeSize:Int = 0


    /**
     * 开始绘制，这个函数指挥执行一次
     * 所以我们在绘制的时候要在这里把所有项都绘制
     * 而不是只处理某一项
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if(mOrientation == VERTICAL_LIST){
            drawVertical(c,parent)
        }else{
            drawHorizontal(c,parent)
        }
    }

    private fun drawHorizontal(c: Canvas,parent: RecyclerView){
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount
        for(i in 0 until childCount){
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + (mDivider?.intrinsicWidth?:0) + (mDividerSize?:0)
            mDivider.setBounds(left,top, right, bottom)
            mDivider.draw(c)
        }
    }

    private fun drawVertical(c: Canvas,parent: RecyclerView){
        // 左边的距离，
        // 意思是左边从哪儿开始绘制，
        // 对于每一项来说，
        // 肯定需要将 RecyclerView 的左边的 paddingLeft 给去掉
        val left = parent.paddingLeft
        // 右边就是 RecyclerView 的宽度减去 RecyclerView 右边设置的 paddingRight 值
        val right = parent.width - parent.paddingRight
        // 获取当前 RecyclerView 下总共有多少 Item
        val childCount = parent.childCount
        // 循环把每一项的都绘制完成，如果最后一项不需要，那么这里的循环就少循环一次
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            // 上边的距离就是当前 Item 下边再加上本身设置的 marginBottom
            val top = child.bottom + params.bottomMargin
            // 下边就简单了，就是上边 + 分割线的高度
            val bottom = top + mDivider.intrinsicHeight + mDividerSize
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

    // 这个函数会被反复执行，执行的次数跟 Item 的个数相同
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val childAdapterPosition = parent.getChildAdapterPosition(view)
        val isFirst = childAdapterPosition == 0
        val isLast = childAdapterPosition == parent.itemDecorationCount - 1
        // 由于在上面的距离绘制，但是实际上那里不会主动为我们绘制腾出空间，
        // 需要重写这个函数来手动调整空间，给上面的绘制不会被覆盖
        if(mOrientation == VERTICAL_LIST){
            when{
                isFirst -> outRect.set(0,mIncludeSize,0, mDivider.intrinsicHeight + mDividerSize)
                isLast -> outRect.set(0,0,0, mDivider.intrinsicHeight + mDividerSize + mIncludeSize)
                else -> outRect.set(0,0,0, mDivider.intrinsicHeight + mDividerSize)
            }
        }else{
            when{
                isFirst -> outRect.set(mIncludeSize,0, mDivider.intrinsicWidth + mDividerSize,0)
                isLast -> outRect.set(mIncludeSize,0, mDivider.intrinsicWidth + mDividerSize + mIncludeSize,0)
                else -> outRect.set(0,0, mDivider.intrinsicWidth + mDividerSize,0)
            }
        }
    }
}