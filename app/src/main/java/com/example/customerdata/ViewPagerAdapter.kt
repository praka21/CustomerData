package com.example.customerdata

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.customerdata.fragments.ResultFragment
import com.example.customerdata.fragments.SearchFragment
import com.example.customerdata.fragments.UploadFragment

class MyPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun getItemCount(): Int {
       return 3;
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return UploadFragment()
            1 -> return SearchFragment()
            2 -> return ResultFragment()
        }
        return SearchFragment()
    }
}