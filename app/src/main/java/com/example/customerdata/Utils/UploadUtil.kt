package com.example.customerdata.Utils

import android.content.Context
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import com.example.customerdata.roomData.Customer
import com.example.customerdata.roomData.CustomerDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import java.io.FileDescriptor
import java.io.FileInputStream

class UploadUtil {

    companion object {

        private var duplicateList: MutableList<Customer> = mutableListOf()

        suspend fun parseCSVData(myFileUri: Uri?, mcontext: Context, database : CustomerDatabase): Boolean {


            if (myFileUri == null) {
                return false
            }

            try {
                val parcelFileDescriptor: ParcelFileDescriptor? =
                    mcontext.contentResolver.openFileDescriptor(myFileUri!!, "r")
                val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor

                val fileInputStream = FileInputStream(fileDescriptor)

                val workbook = HSSFWorkbook(fileInputStream)
                val sheet = workbook.getSheetAt(0)

                for (row in sheet) {
                    var orderDate = 0
                    var orderID = 3
                    var orderquantity = 18
                    var ordername = 20
                    var orderShipname = 21
                    var orderAdd1 = 22
                    var orderAdd2 = 23
                    var orderCity = 24
                    var orderState = 25
                    var orderPincode = 26

                    if (row.rowNum == 0) {
                        val cellIterator: Iterator<Cell> = row.cellIterator()
                        var count: Int = 0
                        while (cellIterator.hasNext()) {
                            val cell = cellIterator.next()
                            //Log.e("Prateek","count at " + count + " data " + cell.toString())
                            when (cell.toString()) {
                                "Ordered On" -> orderDate = count
                                "Order Id" -> orderID = count
                                "Quantity" -> orderquantity = count
                                "Buyer name" -> ordername = count
                                "Ship to name" -> orderShipname = count
                                "Address Line 1" -> orderAdd1 = count
                                "Address Line 2" -> orderAdd2 = count
                                "City" -> orderCity = count
                                "State" -> orderState = count
                                "PIN Code" -> orderPincode = count
                            }
                            count++
                        }


                    } else if (row.rowNum > 0) {
                        val cellIterator: Iterator<Cell> = row.cellIterator()

                        var customer = Customer()
                        var count: Int = 0
                        while (cellIterator.hasNext()) {
                            val cell = cellIterator.next()

                            // Log.e("Prateek","count at " + count + " data " + cell.toString())
                            when (count) {
                                orderDate -> customer.orderDate = cell.toString().trim()
                                orderID -> customer.orderId = cell.toString().trim()
                                orderquantity -> customer.totalQuantity =
                                    cell.toString().toFloat().toInt()
                                ordername -> customer.name = cell.toString().trim()
                                orderShipname -> customer.ShipName = cell.toString().trim()
                                orderAdd1 -> customer.Address1 = cell.toString().trim()
                                orderAdd2 -> customer.Address2 = cell.toString().trim()
                                orderCity -> customer.city =
                                    cell.toString().lowercase().capitalize()
                                orderState -> customer.State =
                                    cell.toString().lowercase().capitalize()
                                orderPincode -> customer.pincode =
                                    cell.toString().toFloat().toInt().toString().trim()
                            }
                            count++

                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            val searchResult = database.customerDao().isCustomerPresent(
                                customer.ShipName,
                                customer.city,
                                customer.State,
                                customer.Address1,
                                customer.Address2
                            )
                            if (searchResult != null && searchResult.size > 0) {

                                val found = database.customerDao().getCustomer(searchResult.get(0).id)
                                if (!found.orderId.contains(customer.orderId)) {
                                    found.orderId = found.orderId + "," + customer.orderId
                                    found.totalQuantity = found.totalQuantity + customer.totalQuantity
                                    database.customerDao().updateCustomer(
                                        found.id,found.orderId , found.totalQuantity, customer.orderDate)
                                    duplicateList.add(found)
                                }else {
                                    database.customerDao().updateCustomer(
                                        found.id, found.orderId , found.totalQuantity + customer.totalQuantity, customer.orderDate)
                                }
                            } else {
                                database.customerDao().insertCustomer(customer)
                            }
                        }.join()

                    }
                }
            } catch (ex: Exception) {
                Log.e("Prateek", "Exception caught$ex")
                return false
            }
            return true
        }


        fun getDuplicates() : MutableList<Customer> {
           return duplicateList
        }

        fun clearDuplicates() {
            duplicateList.clear()
        }

    }

}