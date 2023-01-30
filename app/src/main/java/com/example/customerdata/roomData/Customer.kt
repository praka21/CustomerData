package com.example.customerdata.roomData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer")
data class Customer(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var name: String,
    var ShipName: String,
    var Address1: String,
    var Address2:String,
    var city:String,
    var State:String,
    var pincode:String,
    var orderId: String,
    var totalQuantity: Int,
    var orderDate : String
) {
    constructor() : this(0,"","","", "","","", "", "", 0, "")
}