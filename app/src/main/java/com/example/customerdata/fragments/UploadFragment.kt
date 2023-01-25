package com.example.customerdata.fragments

import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.room.RoomMasterTable.TABLE_NAME
import com.example.customerdata.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.FileReader

class UploadFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload, container, false)
    }


   /* private fun importCSV(){
        val csvReader =
            CSVReader(FileReader("${getExternalStorageDirectory()}/CSV/file.csv"))/* path of local storage (it should be your csv file locatioin)*/
        var nextLine: Array<String> ? = null
        var count = 0
        val columns = StringBuilder()
        GlobalScope.launch(Dispatchers.IO) {
            do {
                val value = StringBuilder()
                nextLine = csvReader.readNext()
                nextLine?.let {nextLine->
                    for (i in 0 until nextLine.size - 1) {
                        if (count == 0) {
                            if (i == nextLine.size - 2) {
                                columns.append(nextLine[i])
                                count =1
                            }
                            else
                                columns.append(nextLine[i]).append(",")
                        } else {                         // this part is for reading value of each row
                            if (i == nextLine.size - 2) {
                                value.append("'").append(nextLine[i]).append("'")
                                count = 2
                            }
                            else
                                value.append("'").append(nextLine[i]).append("',")
                        }
                    }
                    if (count==2) {
                        customerViewModel.pushCustomerData(columns, value)//write here your code to insert all values
                    }
                }
            }while ((nextLine)!=null)
        }

        showToast("Imported SuccessFully",this)
    }*/

}