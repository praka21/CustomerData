package com.example.customerdata.roomData

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CustomerDao {

    @Insert
    fun insertCustomer(customer: Customer)

    @Update
    fun UpdateCustomer(customer: Customer)

    @Delete
    suspend fun deleteCustomer(customer: Customer)

    @Query("SELECT * FROM customer where name LIKE :name")
    suspend fun getCustomers(name: String): List<Customer>

    @Query("SELECT * FROM customer where name = :name and city = :city")
    suspend fun getCustomers(name: String, city: String): List<Customer>

    @Query("SELECT * FROM customer where name = :name and city = :city and State = :state")
    suspend fun getCustomers(name: String, city: String, state: String): List<Customer>

    @Query("SELECT * FROM customer WHERE id = :id")
    fun getCustomer(id: Long): Customer

    @Query("SELECT * FROM customer WHERE ShipName LIKE :name and city LIKE :city and State LIKE :state and Address1 LIKE :adr1 and Address2 LIKE :adr2")
    fun isCustomerPresent(
        name: String,
        city: String,
        state: String,
        adr1: String,
        adr2: String
    ): List<Customer>

    @Query("Update customer SET orderId =:orderid, totalQuantity = :quantity, orderDate =:orderdate WHERE id = :id")
    fun updateCustomer(id: Long, orderid: String, quantity: Int, orderdate: String)

}