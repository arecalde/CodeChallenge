package com.example.codechallenge

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import androidx.core.app.ActivityCompat

import com.google.android.gms.location.LocationServices

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location

import android.widget.Toast

object LocationHelper {
    var PERMISSION_ID = 44
    lateinit var requireContext: () -> Context
    lateinit var requireActivity: () -> Activity

    var locationCallback: ((Location?) -> Unit)? = null


    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

                fusedLocationClient.getLastLocation()
                    .addOnCompleteListener { task ->
                        val location = task.result
                        if (location == null) {
                            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_LONG).show()
                            locationCallback?.let { it(null) }
                        } else {
                            locationCallback?.let { it(location) }
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Please turn on" + " your location...", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                requireContext().startActivity(intent)
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions()
        }
    }


    // method to check for permissions
    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    // method to check
    // if location is enabled
    private fun isLocationEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

}