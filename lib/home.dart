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
                'Battery : $batteryLevel%',
                style: Theme.of(context).textTheme.headline6,
              ),
              SizedBox(height: 24.0),
              ElevatedButton(
                onPressed: getBatteryLevel,
                child: Text("Tap to get Battery Percentaje!"),
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
