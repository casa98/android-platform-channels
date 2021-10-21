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
import android.os.Handler
import android.os.Looper
import io.flutter.plugin.common.EventChannel

class MainActivity: FlutterActivity() {
    private val EVENT_CHANNEL = "com.casa98/charging"
    private lateinit var channel: MethodChannel

    // To send (stream) messages in one direction (Android -> Flutter)
    private lateinit var eventChannel: EventChannel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.TRANSPARENT;

        // Send data to Flutter via 'Streams'
        eventChannel.setStreamHandler(MyStreamHandler(context))

    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        // Initialize stuff to be used with Flutter in this method
        eventChannel = EventChannel(flutterEngine.dartExecutor.binaryMessenger, EVENT_CHANNEL)
    }

    private fun getBatteryLevel(): Int {
        // As I target SDK >= 21, only need this code
        val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }
}

class MyStreamHandler(private  val context: Context): EventChannel.StreamHandler{
    private var receiver: BroadcastReceiver? = null

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        if(events == null) return
        receiver = initReceiver(events)
        context.registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    private fun initReceiver(events: EventChannel.EventSink): BroadcastReceiver {
        return object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent) {
                val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                when(status){
                    BatteryManager.BATTERY_STATUS_CHARGING -> events.success("Battery is Charging")
                    BatteryManager.BATTERY_STATUS_FULL -> events.success("Battery is Full")
                    BatteryManager.BATTERY_STATUS_DISCHARGING -> events.success("Battery is Discharging")
                }
            }

        }
    }

    override fun onCancel(arguments: Any?) {
        context.unregisterReceiver(receiver)
        receiver = null
    }

}