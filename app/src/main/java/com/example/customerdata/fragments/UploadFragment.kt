package com.example.customerdata.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.customerdata.CustomerModel.CustomerViewModel
import com.example.customerdata.Utils.FIleUtil
import com.example.customerdata.R
import com.example.customerdata.Utils.UploadUtil
import com.example.customerdata.roomData.Customer
import com.example.customerdata.roomData.CustomerDatabase
import kotlinx.coroutines.*

class UploadFragment : Fragment() {

    private val MY_RESULT_CODE_FILECHOOSER = 2000

    private lateinit var browseButton : Button
    private lateinit var myPath : EditText
    private lateinit var submit : Button
    private lateinit var result : Button
    private lateinit var viewModel: CustomerViewModel
    private lateinit var database: CustomerDatabase
    private lateinit var callMain : switchInterface


    private var myFileUri : Uri? = null

    override fun onAttach(context: Context) {
        database = CustomerDatabase.getDatabase(context)
        if(context is switchInterface) {
            callMain = context
        }
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
        result = root.findViewById(R.id.result_common)

        val progressDialog = ProgressDialog(requireActivity())
        progressDialog.setTitle("Loading data")
        progressDialog.setMessage("Loading XML...Please wait")

        viewModel = ViewModelProvider(requireActivity()).get(CustomerViewModel::class.java)

        myPath.setText("")
        myFileUri = null
        browseButton.setOnClickListener{
            doBrowseFile();
        }

        submit.setOnClickListener{
            progressDialog.show()

            GlobalScope.launch {

                var output = UploadUtil.parseCSVData(myFileUri, requireActivity(), database)

                requireActivity().runOnUiThread(Runnable {
                    progressDialog.dismiss()
                    if(output) {

                        var duplicates = UploadUtil.getDuplicates()
                        if(duplicates.isEmpty()) {
                            result.visibility = View.VISIBLE
                            result.setText("No repeated Customers found")
                        } else {
                            result.visibility = View.VISIBLE
                            viewModel.result.addAll(duplicates)

                            Log.e("Prateek"," updated result size " + viewModel.result.size)

                            result.setText("" + duplicates.size +" repeated Customers found")
                        }

                        UploadUtil.clearDuplicates()
                        Toast.makeText(activity?.applicationContext, "Import Successfull !!!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(activity?.applicationContext, "Something wrong with File Selected !!!", Toast.LENGTH_LONG).show()
                    }

                })

            }

        }

        result.setOnClickListener{
            callMain.switchtoTabResult()
            myPath.setText("")
            myFileUri = null
            result.setText("result")
            result.visibility = View.INVISIBLE
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