package com.example.codechallenge.home


import android.os.Build
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

    fun checkEmulator() {
        displayData.value = isProbablyRunningOnEmulator().toString()
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

    private fun isProbablyRunningOnEmulator(): Boolean {
        // Android SDK emulator
        return ((Build.FINGERPRINT.startsWith("google/sdk_gphone_")
                && Build.FINGERPRINT.endsWith(":user/release-keys")
                && Build.MANUFACTURER == "Google" && Build.PRODUCT.startsWith("sdk_gphone_") && Build.BRAND == "google"
                && Build.MODEL.startsWith("sdk_gphone_"))
                //
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                //bluestacks
                || "QC_Reference_Phone" == Build.BOARD && !"Xiaomi".equals(
            Build.MANUFACTURER,
            ignoreCase = true
        ) //bluestacks
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.HOST == "Build2" //MSI App Player
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || Build.PRODUCT == "google_sdk")
    }
}