package com.example.customerdata.roomData

import androidx.room.*

@Dao
interface CustomerDao {

    @Insert
    fun insertCustomer(customer: Customer)

    @Update
    fun UpdateCustomer(customer: Customer)

    @Delete
    suspend fun deleteCustomer(customer: Customer)

    @Query("SELECT * FROM customer where UPPER(name) LIKE '%' || :name || '%'")
    suspend fun getCustomers(name: String): MutableList<Customer>

    @Query("SELECT * FROM customer where UPPER(name) like '%' || :name || '%' and UPPER(city) like :city")
    suspend fun getCustomers(name: String, city: String): MutableList<Customer>

    @Query("SELECT * FROM customer where UPPER(name) like '%' || :name || '%' and UPPER(city) like :city and UPPER(State) like :state")
    suspend fun getCustomers(name: String, city: String, state: String): MutableList<Customer>

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