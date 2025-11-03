package com.example.androidweatherapp
import androidx.lifecycle.ViewModel

// async work
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// state management
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.util.Log
import com.example.androidweatherapp.model.WeatherResponse
import dagger.hilt.android.lifecycle.HiltViewModel // hilt annotation for dependency injection
import javax.inject.Inject


sealed class ScreenStates {
    data object Idle: ScreenStates()
    data object Loading: ScreenStates()
    data class ShowData(val icon: Any, val weatherData: WeatherResponse, val aiSuggestion: Any): ScreenStates()
    data class Error(val message: String): ScreenStates()
}

// set up viewmodel here
@HiltViewModel
class ViewModel @Inject constructor(
    private val weatherRepo: WeatherRepository,
    private val aiSuggestRepo: AiSuggestionRepository,
    private val cityRepo: CityRepository
):  ViewModel() {

    // initialising states
    val cityList = cityRepo.getCities()
    private val _uistate = MutableStateFlow<ScreenStates>(ScreenStates.Idle) // mutable
    val uistate: StateFlow<ScreenStates> = _uistate.asStateFlow() // immutable

    fun changeStates(address: String) {
        _uistate.value = ScreenStates.Loading
        Log.d("viewmodel", "address here in 'changeStates' $address")
        viewModelScope.launch{
            // before changing states, need to run validation first
            val weatherValidation = weatherRepo.validation(address) // TODO might need proper validation later
            val weatherData = weatherRepo.getWeatherData(address)


            _uistate.value = ScreenStates.ShowData(icon = "placeholder", weatherData = weatherData, aiSuggestion = "placeholder")


        }
    }

}