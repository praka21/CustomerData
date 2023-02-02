package com.example.customerdata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.customerdata.CustomerModel.CustomerViewModel
import com.example.customerdata.databinding.ActivityMainBinding
import com.example.customerdata.fragments.switchInterface
import com.example.customerdata.roomData.CustomerDatabase
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), switchInterface{

    private lateinit var binding : ActivityMainBinding
    private lateinit var database : CustomerDatabase
    private lateinit var tabLayout: TabLayout
    private lateinit var viewModel: ViewModel
    private lateinit var myPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding  = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tabLayout = binding.tabLayout
        myPager = binding.myViewPager

        viewModel = ViewModelProvider(this).get(CustomerViewModel::class.java)

        val adapter = MyPagerAdapter(supportFragmentManager, lifecycle)
        myPager.adapter = adapter
        database = CustomerDatabase.getDatabase(this)

        TabLayoutMediator(tabLayout, myPager){ tab, position ->
            when (position) {
                0 -> tab.text = "Upload"
                1 -> tab.text = "Search"
                else -> tab.text = "Result"
            }
        }.attach()
    }

    override fun switchtoTabResult() {
        myPager.currentItem = 2
    }

}

