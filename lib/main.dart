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
      title: 'MyApp',
      theme: ThemeData(
        brightness: Brightness.dark,
        scaffoldBackgroundColor: const Color(0xFF141317),
        fontFamily: 'Inter',
        colorScheme: const ColorScheme.dark(
          primary: Color(0xFFE9DDFF),
          secondary: Color(0xFFD0BCFF),
          surface: Color(0xFF201F23),
        ),
      ),
      home: const HomePage(),
    );
  }
}

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  static const Color background = Color(0xFF141317);
  static const Color surface = Color(0xFF201F23);
  static const Color surfaceHigh = Color(0xFF2B292D);
  static const Color primary = Color(0xFFE9DDFF);
  static const Color secondary = Color(0xFFD0BCFF);
  static const Color textPrimary = Color(0xFFE5E1E7);
  static const Color textSecondary = Color(0xFFCAC4D0);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: background,
      bottomNavigationBar: BottomNavigationBar(
        backgroundColor: surface,
        selectedItemColor: primary,
        unselectedItemColor: textSecondary,
        currentIndex: 0,
        type: BottomNavigationBarType.fixed,
        items: const [
          BottomNavigationBarItem(icon: Icon(Icons.home), label: "Home"),
          BottomNavigationBarItem(
            icon: Icon(Icons.calendar_today),
            label: "Schedule",
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.insights),
            label: "Insights",
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.settings),
            label: "Settings",
          ),
        ],
      ),
      appBar: AppBar(
        backgroundColor: background.withValues(alpha: 0.95),
        elevation: 0,
        title: const Text(
          "Daily Overview",
          style: TextStyle(
            color: primary,
            fontSize: 34,
            fontWeight: FontWeight.w700,
          ),
        ),
        actions: const [
          Padding(
            padding: EdgeInsets.only(right: 16),
            child: Icon(Icons.notifications_none, color: primary),
          ),
        ],
      ),
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // const SizedBox(height: 12),

              // Greeting
              // const Text(
              //   "Good Morning.",
              //   style: TextStyle(
              //     fontSize: 34,
              //     fontWeight: FontWeight.bold,
              //     color: textPrimary,
              //   ),
              // ),

              // const SizedBox(height: 8),

              // const Text(
              //   "Your biological rhythm is currently optimized for focused work.",
              //   style: TextStyle(
              //     fontSize: 16,
              //     color: textSecondary,
              //   ),
              // ),

              // const SizedBox(height: 28),

              // Energy Card
              Container(
                width: double.infinity,
                padding: const EdgeInsets.all(24),
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(28),
                  gradient: LinearGradient(
                    colors: [
                      secondary.withValues(alpha: 0.18),
                      const Color(0xFF4F378A).withValues(alpha: 0.35),
                    ],
                    begin: Alignment.topLeft,
                    end: Alignment.bottomRight,
                  ),
                  border: Border.all(
                    color: Colors.white.withValues(alpha: 0.06),
                  ),
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: const [
                        Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              "Estimated energy",
                              style: TextStyle(
                                fontSize: 22,
                                fontWeight: FontWeight.w600,
                                color: textPrimary,
                              ),
                            ),
                            SizedBox(height: 4),
                            Text(
                              "Based on last night's rest",
                              style: TextStyle(color: textSecondary),
                            ),
                          ],
                        ),
                        Icon(Icons.bolt, color: primary, size: 36),
                      ],
                    ),

                    const SizedBox(height: 26),

                    Center(
                      child: SizedBox(
                        width: 140,
                        height: 140,
                        child: Stack(
                          alignment: Alignment.center,
                          children: [
                            SizedBox(
                              width: 140,
                              height: 140,
                              child: CircularProgressIndicator(
                                value: 0.72,
                                strokeWidth: 12,
                                backgroundColor: surfaceHigh,
                                valueColor: const AlwaysStoppedAnimation<Color>(
                                  primary,
                                ),
                              ),
                            ),
                            const Text(
                              "72%",
                              style: TextStyle(
                                fontSize: 46,
                                fontWeight: FontWeight.bold,
                                color: primary,
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
              ),

              const SizedBox(height: 30),

              // Schedule Header
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: const [
                  Text(
                    "Today's Schedule",
                    style: TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.w600,
                      color: textPrimary,
                    ),
                  ),
                  Text(
                    "View All",
                    style: TextStyle(
                      color: primary,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                ],
              ),

              const SizedBox(height: 20),

              // Alert Banner
              Container(
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  color: surfaceHigh,
                  borderRadius: BorderRadius.circular(16),
                  border: const Border(
                    left: BorderSide(color: Color(0xFFFFB4AB), width: 4),
                  ),
                ),
                child: Row(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: const [
                    Icon(Icons.warning_amber_rounded, color: Color(0xFFFFB4AB)),
                    SizedBox(width: 12),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            "Energy Dip Expected",
                            style: TextStyle(
                              fontWeight: FontWeight.bold,
                              color: textPrimary,
                            ),
                          ),
                          SizedBox(height: 6),
                          Text(
                            "Energy is predicted to be lower around 2:00 PM. Consider postponing low-priority tasks.",
                            style: TextStyle(color: textSecondary),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),

              const SizedBox(height: 20),

              // Tasks
              _buildTaskCard(
                title: "Project Presentation",
                time: "10:00 AM - 11:30 AM",
                priority: "High",
                priorityColor: Colors.redAccent,
              ),

              const SizedBox(height: 14),

              _buildTaskCard(
                title: "Weekly Sync",
                time: "1:00 PM - 2:00 PM",
                priority: "Medium",
                priorityColor: const Color(0xFF7E57C2),
              ),

              const SizedBox(height: 14),

              _buildTaskCard(
                title: "Inbox Zero",
                time: "2:30 PM - 3:30 PM",
                priority: "Low",
                priorityColor: Colors.grey,
                showPostpone: true,
              ),

              const SizedBox(height: 40),
            ],
          ),
        ),
      ),
    );
  }

  static Widget _buildTaskCard({
    required String title,
    required String time,
    required String priority,
    required Color priorityColor,
    bool showPostpone = false,
  }) {
    return Container(
      padding: const EdgeInsets.all(18),
      decoration: BoxDecoration(
        color: surface,
        borderRadius: BorderRadius.circular(22),
        border: Border.all(color: Colors.white.withValues(alpha: 0.05)),
      ),
      child: Column(
        children: [
          Row(
            children: [
              Container(
                width: 24,
                height: 24,
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  border: Border.all(color: textSecondary),
                ),
              ),

              const SizedBox(width: 14),

              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Wrap(
                      spacing: 8,
                      crossAxisAlignment: WrapCrossAlignment.center,
                      children: [
                        Text(
                          title,
                          style: const TextStyle(
                            fontSize: 16,
                            color: textPrimary,
                            fontWeight: FontWeight.w500,
                          ),
                        ),
                        Container(
                          padding: const EdgeInsets.symmetric(
                            horizontal: 8,
                            vertical: 4,
                          ),
                          decoration: BoxDecoration(
                            color: priorityColor.withValues(alpha: 0.2),
                            borderRadius: BorderRadius.circular(6),
                          ),
                          child: Text(
                            priority.toUpperCase(),
                            style: TextStyle(
                              fontSize: 10,
                              fontWeight: FontWeight.bold,
                              color: priorityColor,
                            ),
                          ),
                        ),
                      ],
                    ),

                    const SizedBox(height: 6),

                    Text(
                      time,
                      style: const TextStyle(
                        color: textSecondary,
                        fontSize: 13,
                      ),
                    ),
                  ],
                ),
              ),

              const Icon(Icons.chevron_right, color: textSecondary),
            ],
          ),

          if (showPostpone) ...[
            const SizedBox(height: 18),
            Align(
              alignment: Alignment.centerRight,
              child: OutlinedButton.icon(
                onPressed: () {},
                icon: const Icon(Icons.update, size: 18),
                label: const Text("Postpone"),
                style: OutlinedButton.styleFrom(
                  foregroundColor: textPrimary,
                  side: BorderSide(color: Colors.white.withValues(alpha: 0.12)),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(30),
                  ),
                ),
              ),
            ),
          ],
        ],
      ),
    );
  }
}
