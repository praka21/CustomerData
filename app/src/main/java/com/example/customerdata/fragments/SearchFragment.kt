package com.example.customerdata.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.customerdata.databinding.FragmentSearchBinding
import com.example.customerdata.roomData.Customer
import com.example.customerdata.roomData.CustomerDatabase


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: CustomerDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        binding.search.setOnClickListener{
            var name_to_search: String = binding.name.text.toString()
            var state_to_search: String = binding.state.text.toString()
            var city_to_search: String = binding.city.text.toString()
            queryDatabase(name_to_search, state_to_search, city_to_search)
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    override fun onAttach(context: Context) {
        database = CustomerDatabase.getDatabase(context)
        super.onAttach(context)
    }

    private fun queryDatabase(nameToSearch: String, stateToSearch: String, cityToSearch: String) {
        var output = "null ";
        if(nameToSearch.equals("") || nameToSearch == null) {
            output = " Cannot search without name!! You idiot"
        } else if (stateToSearch.equals("") || cityToSearch.equals("")) {
            val customer: List<Customer> = database.customerDao().getCustomers(nameToSearch)
            output = " " + customer.size + " record found"
        } else {
            val customer: List<Customer> = database.customerDao().getCustomers(nameToSearch,cityToSearch,stateToSearch)
            output = " " + customer.size + " record found"
        }

        requireActivity().runOnUiThread(Runnable {
            Toast.makeText(context, output, Toast.LENGTH_LONG).show()
        })
    }

}