package com.example.customerdata.CustomerModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.customerdata.roomData.Customer
import com.example.customerdata.roomData.CustomerDatabase

class CustomerViewModel : ViewModel() {
    var result: List<Customer>? = null

}