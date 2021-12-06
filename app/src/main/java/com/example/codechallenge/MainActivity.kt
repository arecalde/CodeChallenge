package com.example.codechallenge

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.codechallenge.helpers.LocationHelper
import com.example.codechallenge.helpers.SharedPreferencesWrapper


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LocationHelper.requireActivity = { this }
        LocationHelper.requireContext = { this }
        SharedPreferencesWrapper.requireActivity = { this }
    }

    // If everything is alright then
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LocationHelper.PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LocationHelper.getLastLocation()
            }
        }
    }
}