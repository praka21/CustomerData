package com.example.customerdata.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.customerdata.CustomerModel.CustomerViewModel
import com.example.customerdata.databinding.FragmentSearchBinding
import com.example.customerdata.roomData.Customer
import com.example.customerdata.roomData.CustomerDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: CustomerDatabase
    private lateinit var viewModel: CustomerViewModel

    private lateinit var switchInterface: switchInterface

    private lateinit var result_view : TextView
    private var resultList : MutableList<Customer> = mutableListOf()

    private lateinit var stateList : MutableList<String>
    private lateinit var cityList : MutableList<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        var imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        GlobalScope.launch {
            stateList = database.stateDao().getStates()!!
            requireActivity().runOnUiThread(Runnable {
                val adapter = ArrayAdapter<String>(requireActivity(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, stateList)
                binding.state.setAdapter(adapter)
            })
        }

        GlobalScope.launch {
            cityList = database.stateDao().getCity()!!
            requireActivity().runOnUiThread(Runnable {
                val adapter = ArrayAdapter<String>(requireActivity(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, cityList)
                binding.city.setAdapter(adapter)
            })
        }

        result_view = binding.resultFound

        binding.search.setOnClickListener{
            var name_to_search: String = binding.name.text.toString()
            var state_to_search: String = binding.state.text.toString()
            var city_to_search: String = binding.city.text.toString()
            GlobalScope.launch {
                queryDatabase(name_to_search, state_to_search, city_to_search)
            }

        }

        viewModel = ViewModelProvider(requireActivity()).get(CustomerViewModel::class.java)
        result_view.setOnClickListener{
            viewModel.result = resultList
            switchInterface.switchtoTabResult()
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        database = CustomerDatabase.getDatabase(context)
        if(context is switchInterface) {
            switchInterface = context
        }
        super.onAttach(context)
    }

    private suspend fun queryDatabase(nameToSearch: String, stateToSearch: String, cityToSearch: String) {
        var output = "";
        if(nameToSearch.equals("")) {
            output = " Cannot search without name!! You idiot"
        } else if (stateToSearch.equals("") || cityToSearch.equals("")) {
            val job = GlobalScope.launch {
                resultList = database.customerDao().getCustomers(nameToSearch.uppercase())
            }.join()

            output = "" + resultList?.size + " record found"

        } else {
            val job = GlobalScope.launch {
                resultList = database.customerDao().getCustomers(nameToSearch.uppercase(), cityToSearch.uppercase(), stateToSearch.uppercase())
            }.join()
            output = " " + resultList.size + " record found"
        }



        requireActivity().runOnUiThread(Runnable {
            result_view.text = "" + resultList.size + " record found"
            result_view.visibility = View.VISIBLE
            Toast.makeText(context, output, Toast.LENGTH_LONG).show()
        })
    }

}