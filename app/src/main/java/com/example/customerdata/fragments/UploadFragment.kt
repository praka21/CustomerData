package com.example.customerdata.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.FileUtils
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.customerdata.FIleUtil
import com.example.customerdata.R
import com.example.customerdata.roomData.Customer
import com.example.customerdata.roomData.CustomerDatabase
import kotlinx.coroutines.*
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import kotlin.math.roundToInt


class UploadFragment : Fragment() {

    private val MY_RESULT_CODE_FILECHOOSER = 2000

    private lateinit var browseButton : Button
    private lateinit var myPath : EditText
    private lateinit var submit : Button
    private lateinit var database: CustomerDatabase


    private var myFileUri : Uri? = null

    override fun onAttach(context: Context) {
        database = CustomerDatabase.getDatabase(context)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val root: View = inflater.inflate(R.layout.fragment_upload, container, false)

        myPath = root.findViewById(R.id.selectedString)
        browseButton = root.findViewById(R.id.select)
        submit = root.findViewById(R.id.submit_data)

        browseButton.setOnClickListener{
            doBrowseFile();
        }

        submit.setOnClickListener{
            uploadCSVData()
        }
        return root
    }


    private fun doBrowseFile() {
        var chooseFileIntent = Intent(Intent.ACTION_GET_CONTENT)
        chooseFileIntent.type = "*/*"
        // Only return URIs that can be opened with ContentResolver
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE)
        chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file")
        startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILECHOOSER)
    }


    private fun uploadCSVData(){

        if(myFileUri == null) {
            Toast.makeText(this.context, "Please Select file !!", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val parcelFileDescriptor: ParcelFileDescriptor? =
                requireContext().contentResolver.openFileDescriptor(myFileUri!!, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor

            val fileInputStream = FileInputStream(fileDescriptor)

            val workbook = HSSFWorkbook(fileInputStream)
            val sheet = workbook.getSheetAt(0)

            for(row in sheet) {

                if(row.rowNum > 0) {
                    var colnumber = 0
                    val cellIterator : Iterator<Cell> = row.cellIterator()

                    var customer = Customer()
                    var count : Int = 0
                    while (cellIterator.hasNext()) {
                        val cell = cellIterator.next()
                        when(count) {
                            0 -> customer.orderDate = cell.toString().trim()
                            3 -> customer.orderId = cell.toString().trim()
                            17 -> customer.totalQuantity = cell.toString().toFloat().toInt()
                            19 -> customer.name = cell.toString().trim()
                            20 -> customer.ShipName = cell.toString().trim()
                            21 -> customer.Address1 = cell.toString().trim()
                            22 -> customer.Address2 = cell.toString().trim()
                            23 -> customer.city = cell.toString().trim()
                            24 -> customer.State = cell.toString().trim()
                            25 -> customer.pincode = cell.toString().trim()

                        }
                        count ++

                    }
                    CoroutineScope(Dispatchers.IO).launch {

                        val searchResult = database.customerDao().isCustomerPresent(customer.ShipName, customer.city, customer.State, customer.Address1, customer.Address2)

                        Log.e("Prateek", " search result  size is "+ searchResult.size)

                        if ( searchResult != null && searchResult.size > 0) {

                            Log.e("Prateek", " search result id"+ searchResult.get(0).id)
                            val found = database.customerDao().getCustomer(searchResult.get(0).id)

                            database.customerDao().updateCustomer(found.id,found.orderId + ", "+ customer.orderId, found.totalQuantity + customer.totalQuantity,
                                customer.orderDate)
                            Log.e("Prateek", " update operation successfull")

                        } else {
                            Log.e("Prateek", " insert operation ")
                            val job = database.customerDao().insertCustomer(customer)

                            Log.e("Prateek", " insert operation successfull")
                        }
                    }

                }
            }
        } catch (ex : Exception) {
            Log.e("Prateek"," Exception caught" + ex)
            Toast.makeText(this.context, "Not imported", Toast.LENGTH_LONG).show()
            return
        }
        Toast.makeText(this.context, "Imported Successfully", Toast.LENGTH_LONG).show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            MY_RESULT_CODE_FILECHOOSER -> if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val fileUri: Uri? = data.data
                    Log.i("Prateek", "Uri: $fileUri")
                    var filePath: String? = null
                    try {
                        filePath = fileUri?.path
                    } catch (e: Exception) {
                        Log.e("Prateek", "Error: $e")
                        Toast.makeText(this.context, "Error: $e", Toast.LENGTH_SHORT).show()
                    }
                    myPath.setText(FIleUtil.getPath(requireContext(),fileUri))
                    myFileUri = fileUri
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        myFileUri = null
    }

}