import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class HomePage extends StatefulWidget {
  const HomePage();

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  static const batteryChannel = MethodChannel("com.casa98/battery");
  int batteryLevel = 0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(
                'Counter: $batteryLevel',
                style: Theme.of(context).textTheme.headline6,
              ),
              SizedBox(height: 24.0),
              ElevatedButton(
                onPressed: getBatteryLevel,
                child: Text("Tap to Count!"),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Future getBatteryLevel() async {
    final newBatteryLevel =
        await batteryChannel.invokeMethod("getBatteryLevel", batteryLevel);
    setState(() => batteryLevel = newBatteryLevel);
  }
}
