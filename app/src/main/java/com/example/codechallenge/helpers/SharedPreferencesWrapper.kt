package com.example.codechallenge.helpers

import android.app.Activity
import android.content.SharedPreferences
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import com.example.codechallenge.home.HomeFragment
import com.google.gson.Gson

private const val LOCATION_KEY = "location"
private const val APPS_LIST_KEY = "apps"
private const val BATTERY_STATE_KEY = "battery"
object SharedPreferencesWrapper {
    lateinit var requireActivity: () -> Activity
    val prefs: SharedPreferences
        get() = requireActivity().getSharedPreferences(
            "com.example.codechallenge", AppCompatActivity.MODE_PRIVATE)

    private val gson = Gson()

    fun setBatteryInfo(batteryInfo: BatteryInfo) {
        prefs.edit()?.putString(BATTERY_STATE_KEY, gson.toJson(batteryInfo).encodeData())?.apply()
    }

    fun getBatteryInfo(): BatteryInfo? {
        val json = prefs.getString(BATTERY_STATE_KEY, "")
        return gson.fromJson(json?.decodeData(), BatteryInfo::class.java)
    }

    fun setLocation(location: Location) {
        prefs.edit()?.putString(LOCATION_KEY, gson.toJson(location).encodeData())?.apply()
    }

    fun getLocation(): Location? {
        val json = prefs.getString(LOCATION_KEY, null)
        return gson.fromJson(json?.decodeData(), Location::class.java)
    }

    fun setAppsList(apps: List<String>) {
        val set: MutableSet<String> = HashSet()
        set.addAll(apps.map { it.encodeData() } )
        prefs.edit().putStringSet(APPS_LIST_KEY, set).apply()
    }

    fun getAppsList(): List<String>? {
        return prefs.getStringSet(APPS_LIST_KEY, null)?.map { it.decodeData() }
    }

}

data class BatteryInfo(val batteryLevel: Int, val pluggedIn: Boolean)
