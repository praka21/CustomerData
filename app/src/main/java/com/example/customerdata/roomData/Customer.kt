package com.example.customerdata.roomData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer")
data class Customer(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val ShipName: String,
    val Address1: String,
    val Address2:String,
    val city:String,
    val State:String,
    val pincode:String,
    val orderId: String,
    val totalQuantity: Int
)