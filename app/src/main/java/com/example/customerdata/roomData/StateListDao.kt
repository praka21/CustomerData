package com.example.customerdata.roomData

import androidx.room.*

@Dao
interface StateListDao {

    @Query("SELECT state from customer GROUP BY state")
    fun getStates() : MutableList<String>?

    @Query("SELECT city from customer GROUP BY city")
    fun getCity() : MutableList<String>?

}