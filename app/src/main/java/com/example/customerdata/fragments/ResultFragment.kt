package com.example.customerdata.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customerdata.CustomerModel.CustomerViewModel
import com.example.customerdata.R


class ResultFragment : Fragment() {

    lateinit var myView : RecyclerView
    lateinit var provider : CustomerViewModel
    var adapter : ResultViewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_result, container, false)

        provider = ViewModelProvider(requireActivity()).get(CustomerViewModel::class.java)
        myView = root.findViewById(R.id.resultview)


        Log.e("Prateek", "createview called")
        adapter = ResultViewAdapter(provider.result)
        myView.layoutManager = LinearLayoutManager(requireActivity().baseContext)
        myView.adapter = adapter

        return root
    }

    override fun onResume() {
        super.onResume()
        Log.e("Prateek", "onresume called")

        if(adapter != null) {
            adapter!!.setData(provider.result)
        } else {
            Log.e("Prateek", "adapter is null")
        }

    }

    override fun onDestroyView() {
        Log.e("Prateek", "view destroyed")
        super.onDestroyView()

    }



}