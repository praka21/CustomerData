package com.example.customerdata.roomData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Customer::class], version = 1)
abstract class CustomerDatabase : RoomDatabase() {

    abstract fun customerDao() : CustomerDao

    companion object{
        @Volatile
        private var Instance : CustomerDatabase? = null

        fun getDatabase(contaxt: Context): CustomerDatabase{
            if(Instance == null) {
                synchronized(this) {
                    Instance = Room.databaseBuilder(
                        contaxt.applicationContext, CustomerDatabase::class.java,
                        "customerDataBase"
                    ).build()
                }
            }
            return Instance!!

        }

    }

}