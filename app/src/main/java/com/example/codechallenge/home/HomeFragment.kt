package com.example.codechallenge.home

import android.content.*
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.codechallenge.databinding.HomeFragmentBinding
import androidx.fragment.app.viewModels
import com.example.codechallenge.helpers.BatteryInfo
import com.example.codechallenge.helpers.LocationHelper
import com.example.codechallenge.helpers.SharedPreferencesWrapper
import com.google.gson.Gson



class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    val gson = Gson()
    private var checkBatteryInfo = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = HomeFragmentBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.getBatteryData.observeEvent(viewLifecycleOwner) {
            checkBatteryInfo = true
            val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)

            requireActivity().registerReceiver(batteryBroadcastReceiver, intentFilter)
        }

        viewModel.getPackages.observeEvent(viewLifecycleOwner) {
            val pm = requireActivity().packageManager
            val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA).map { it.packageName }
            SharedPreferencesWrapper.setAppsList(packages)
        }

        LocationHelper.locationCallback = { location ->
            location?.let {
                SharedPreferencesWrapper.setLocation(it)
            }
        }

        return binding.root
    }

    // If everything is alright then
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == LocationHelper.PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LocationHelper.getLastLocation()
            }
        }
    }

    private val batteryBroadcastReceiver: BroadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "android.intent.action.BATTERY_CHANGED" && context != null && checkBatteryInfo) {
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                SharedPreferencesWrapper.setBatteryInfo(BatteryInfo(level, Power.isConnected(context)))
                checkBatteryInfo = false
            }
        }
    }

    object Power {
        fun isConnected(context: Context): Boolean {
            val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val plugged = intent!!.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
            return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB || plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(batteryBroadcastReceiver)
    }

}