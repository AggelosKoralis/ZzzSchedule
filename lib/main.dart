import 'package:flutter/material.dart';

void main() {
    runApp(const MyApp());
}

class MyApp extends StatelessWidget {
    const MyApp({super.key});

    @override
    Widget build(BuildContext context) {
        return MaterialApp(
            debugShowCheckedModeBanner: false,

            theme: ThemeData(
                    useMaterial3: true,
                    colorSchemeSeed: Colors.deepPurple,
                    brightness: Brightness.dark,
                ),
            
            home: const HomeScreen(),
        );
    }
}

class HomeScreen extends StatelessWidget {
    const HomeScreen({super.key});

    @override
    Widget build(BuildContext context) {
        return Scaffold(
            appBar: AppBar(
                title: const Text("Material 3 Demo"),
            ),

            body: Padding(
                padding: const EdgeInsets.all(20),

                child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                        Text(
                            "Welcome",
                            style: Theme.of(context).textTheme.headlineMedium,
                        ),

                        const SizedBox(height: 20),

                        Card(
                            child: Padding(
                                padding: const EdgeInsets.all(20),
                                child: Text(
                                    "This is a Material 3 card.",
                                ),
                            ),
                        ),

                        const SizedBox(height: 20),

                        FilledButton(
                            style: ButtonStyle(
                                backgroundColor: WidgetStateProperty.resolveWith<Color>(
                                    (states) {
                                        if (states.contains(WidgetState.pressed)) {
                                            return Colors.red;
                                        }

                                        return Colors.blueAccent;
                                    },
                                ),
                            ),
                            onPressed: () {},
                            child: const Text("Continue"),
                        ),
                    ],
                ),
            ),
        );
    }
}
