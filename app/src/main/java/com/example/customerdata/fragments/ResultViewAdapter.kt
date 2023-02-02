package com.example.customerdata.fragments

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.customerdata.R
import com.example.customerdata.roomData.Customer

class ResultViewAdapter: RecyclerView.Adapter<ResultViewAdapter.ViewHelper> {

    private var data : MutableList<Customer>?

    constructor(data : List<Customer>?) {
        this.data = data as MutableList<Customer>?

    }

    class ViewHelper(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = itemView.findViewById<TextView>(R.id.itemName)
        var address = itemView.findViewById<TextView>(R.id.itemAdrress)
        var citystate =itemView.findViewById<TextView>(R.id.itemCityState)
        var orderid =itemView.findViewById<TextView>(R.id.itemOrders)
    }

    fun setData(newList : List<Customer>?) {
        data = newList as MutableList<Customer>?
        Log.e("Prateek", "added element "+ (data?.size ?: 0))
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHelper {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ViewHelper(view)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHelper, position: Int) {
        holder.name.text = data!!.get(position).ShipName
        holder.address.text = data!!.get(position).Address1 + ", "+ data!!.get(position).Address2
        holder.citystate.text = data!!.get(position).city + ", " + data!!.get(position).State
        holder.orderid.text = data!!.get(position).orderId
    }


}