package com.example.customerdata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.example.customerdata.databinding.ActivityMainBinding
import com.example.customerdata.roomData.Customer
import com.example.customerdata.roomData.CustomerDatabase
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var database : CustomerDatabase
    private lateinit var tabLayout: TabLayout
    private lateinit var myPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding  = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tabLayout = binding.tabLayout
        myPager = binding.myViewPager

        val adapter = MyPagerAdapter(supportFragmentManager, lifecycle)
        myPager.adapter = adapter
        database = CustomerDatabase.getDatabase(this)

        TabLayoutMediator(tabLayout, myPager){ tab, position ->
            if(position == 0) tab.text = "Upload"
            else if(position ==1) tab.text = "Search"
            else tab.text = "Result"
        }.attach()





    }

}

