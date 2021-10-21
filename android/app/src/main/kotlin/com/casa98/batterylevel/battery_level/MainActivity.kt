package com.casa98.batterylevel.battery_level

import android.content.*
import io.flutter.embedding.android.FlutterActivity
import androidx.annotation.NonNull
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import android.os.BatteryManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES

class MainActivity: FlutterActivity() {
    private val BATTERY_CHANNEL = "com.casa98/battery"
    private lateinit var channel: MethodChannel

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, BATTERY_CHANNEL)

        /// Receive data from Flutter
        channel.setMethodCallHandler {call, result ->
            if(call.method == "getBatteryLevel"){
                // Get value coming from Flutter
                var counter = call.arguments() as Int
                result.success(++counter)
            }
        }
    }
}
