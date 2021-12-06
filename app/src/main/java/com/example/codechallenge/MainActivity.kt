package com.example.codechallenge

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.BatteryManager
import android.widget.TextView
import android.content.pm.PackageManager

import com.example.codechallenge.LocationHelper.PERMISSION_ID

class MainActivity : AppCompatActivity() {
    private var resultView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resultView = findViewById(R.id.result)

        LocationHelper.requireActivity = { this }
        LocationHelper.requireContext = { this }
        LocationHelper.locationCallback = {
            val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            registerReceiver(batteryBroadcastReceiver, intentFilter)
            val pm = packageManager
            val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            println(packages)
        }

        LocationHelper.getLastLocation()
    }

    // If everything is alright then
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LocationHelper.getLastLocation()
            }
        }
    }

    private val batteryBroadcastReceiver: BroadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "android.intent.action.BATTERY_CHANGED" && context != null) {
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                resultView?.text = "$level ${Power.isConnected(context)}"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryBroadcastReceiver)
    }

    object Power {
        fun isConnected(context: Context): Boolean {
            val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val plugged = intent!!.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
            return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB || plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS
        }
    }

}