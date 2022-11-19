package me.wkbin.movie.app.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import me.wkbin.movie.app.ui.fm.HomeFragment
import me.wkbin.movie.app.ui.fm.MyFragment
import javax.inject.Inject

class MainAdapter @Inject constructor(act:FragmentActivity):FragmentStateAdapter(act) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0){
            HomeFragment.newInstance()
        }else{
            MyFragment.newInstance()
        }
    }
}