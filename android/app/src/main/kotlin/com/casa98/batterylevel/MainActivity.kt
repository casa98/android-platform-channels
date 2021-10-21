package com.casa98.batterylevel

import android.content.*
import android.graphics.Color
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import android.os.BatteryManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle

class MainActivity: FlutterActivity() {
    private val BATTERY_CHANNEL = "com.casa98/battery"
    private lateinit var channel: MethodChannel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.TRANSPARENT;
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, BATTERY_CHANNEL)

        /// Receive data from Flutter
        channel.setMethodCallHandler {call, result ->
            if(call.method == "getBatteryLevel"){
                // Get value coming from Flutter
                val batteryLevel = getBatteryLevel()
                result.success(batteryLevel)
            }
        }
    }

    private fun getBatteryLevel(): Int {
        // As I target SDk >= 21, only need this code
        val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }
}
