package com.example.customerdata.fragments

import android.app.Activity
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
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream


class UploadFragment : Fragment() {

    private val MY_RESULT_CODE_FILECHOOSER = 2000

    private lateinit var browseButton : Button
    private lateinit var myPath : EditText
    private lateinit var submit : Button

    private var myFileUri : Uri? = null

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
                    while (cellIterator.hasNext()) {
                        val cell = cellIterator.next()
                        Log.e("Prateek",cell.toString())
                    }


                }
            }


        } catch (ex : Exception) {
            Log.e("Prateek"," Exception caught" + ex.printStackTrace())
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

}