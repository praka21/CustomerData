package com.example.customerdata.roomData

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CustomerDao {

    @Insert
    suspend fun insertCustomer(customer: Customer)

    @Update
    suspend fun UpdateCustomer(customer: Customer)

    @Delete
    suspend fun deleteCustomer(customer: Customer)

    @Query("SELECT * FROM customer where name = :name")
    fun getCustomers(name:String) : List<Customer>

    @Query("SELECT * FROM customer where name = :name and city = :city")
    fun getCustomers(name:String, city:String) : List<Customer>

    @Query("SELECT * FROM customer where name = :name and city = :city and State = :state")
    fun getCustomers(name:String, city:String, state:String) : List<Customer>

}