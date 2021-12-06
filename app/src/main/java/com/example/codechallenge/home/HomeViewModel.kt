package com.example.codechallenge.home


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.codechallenge.helpers.Event
import com.example.codechallenge.helpers.LocationHelper
import com.example.codechallenge.helpers.SharedPreferencesWrapper

class HomeViewModel : ViewModel() {
    val getPackages = Event(Unit)
    val getBatteryData = Event(Unit)

    val displayData = MutableLiveData("")
    fun setData() {
        LocationHelper.getLastLocation()
        getPackages.raiseEvent(Unit)
        getBatteryData.raiseEvent(Unit)
    }

    fun getData() {
        val battery = SharedPreferencesWrapper.getBatteryInfo()
        val location = SharedPreferencesWrapper.getLocation()
        val apps = SharedPreferencesWrapper.getAppsList()

        var displayDataBuilder = ""
        battery?.let {
            displayDataBuilder = displayDataBuilder.plus("Battery: ${it.batteryLevel}%\n")
            displayDataBuilder = displayDataBuilder.plus("Charging: ${it.pluggedIn}\n\n")
        }

        location?.let {
            displayDataBuilder = displayDataBuilder.plus("Lat: ${it.latitude}\n")
            displayDataBuilder = displayDataBuilder.plus("Long: ${it.longitude}\n\n")
        }

        apps?.let { set ->
            displayDataBuilder = displayDataBuilder.plus("Apps: \n")

            set.forEach {
                displayDataBuilder = displayDataBuilder.plus("$it\n")
            }
        }

        displayData.value = displayDataBuilder
    }
}