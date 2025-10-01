package com.example.androidweatherapp


import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import android.widget.ArrayAdapter // for creating a view out of a list
import android.widget.AutoCompleteTextView

import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewmodel: ViewModel by viewModels() // life-cycle aware delegation of ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainlayout) // inflate layout on creation of Activity

        // picking layout elements from the View
        val icon = findViewById<ImageView>(R.id.weatherIcon)
        val weather = findViewById<TextView>(R.id.weatherDetails)
        val aiSuggestions = findViewById<TextView>(R.id.aiSuggestions)
        val cities = viewmodel.cityList // data comes from repository, through viewmodel
        val arrAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cities) // initialising array adapter with a dropdown xml layout. simple_dropdown_item_1line is an in-built layout
        val address = findViewById<AutoCompleteTextView>(R.id.postalAddress)
        address.setAdapter(arrAdapter)
        address.setOnItemClickListener{parent, view, position, id -> // here parent is of type AdapterView (which is an abstract class), so now we have access to its methods
            val addressItem = parent.getItemAtPosition(position) // using AdapterView.getItemAtPosition() method
            viewmodel.changeStates(addressItem.toString())
        }


        // asynchronous so state changes don't stop the main thread from running
        lifecycleScope.launch {
            // collect states here
            viewmodel.uistate.collectLatest { state ->
                when(state) {
                    is ScreenStates.Idle -> {
                        // do something
                    }
                    is ScreenStates.Loading -> {
                        // do something
                        weather.text = "Weather is "
                    }
                    is ScreenStates.ShowData -> {
                        // do something
                        weather.text = state.weatherData.toString()
                    }
                    is ScreenStates.Error -> {
                        // do something
                    }
                }
            }

        }




    }
}



