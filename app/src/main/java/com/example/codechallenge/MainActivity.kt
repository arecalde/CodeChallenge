package com.example.codechallenge

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
}