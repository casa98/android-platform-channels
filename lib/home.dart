import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class HomePage extends StatefulWidget {
  const HomePage();

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  late StreamSubscription _streamSubscription;
  static const batteryChannel = MethodChannel("com.casa98/battery");
  static const charginChannel = EventChannel("com.casa98/charging");
  String batteryLevel = 'Waiting Stream...';

  @override
  void didChangeDependencies() {
    //onListenBattery();  // Returns current device battery percentage
    onStreamBattery();
    super.didChangeDependencies();
  }

  void onListenBattery() {
    batteryChannel.setMethodCallHandler((call) async {
      if (call.method == "reportBatteryLevel") {
        final int batteryLevel = call.arguments;
        setState(() => this.batteryLevel = '$batteryLevel');
      }
    });
  }

  void onStreamBattery() {
    _streamSubscription =
        charginChannel.receiveBroadcastStream().listen((event) {
      print('event coming: $event');
      setState(() => batteryLevel = '$event');
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: IconButton(
          onPressed: () {},
          icon: Icon(Icons.arrow_back),
          splashRadius: 20.0,
          splashColor: Colors.transparent,
        ),
      ),
      body: SafeArea(
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(
                '$batteryLevel',
                style: Theme.of(context).textTheme.headline6,
              ),
            ],
          ),
        ),
      ),
    );
  }

  @override
  void dispose() {
    _streamSubscription.cancel();
    super.dispose();
  }
}
