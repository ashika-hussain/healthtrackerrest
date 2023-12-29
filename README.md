# HealthTracker Application

**Health Tracker app version:** V5.0

Health Tracker is a Kotlin-based web application that provides features for tracking user health and wellness. It offers functionalities such as user management, BMI calculation, random health tips, User-sleep Tracker, User-Target, User-sleep duration, and user activity tracking.

## Features

- **User Management:** Add, list, and manage users in the system.
- **Biometric Tracker:** Track biometrics of user
- **User Activity Tracking:** Track and manage user activities.
- **Levels** Get better in activities
- **User-Goals:** Set goals for motivation
- **CaloriE-Tracker**To calculate the calorie intake

## Prerequisites

Before you get started, make sure you have the following prerequisites:

- [Java 20 (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html): Make sure you have Java 11 or a compatible version installed.
- [Javalin 5.6.0](https://javalin.io/)
- [Maven 4.0.0](https://maven.apache.org/download.cgi)

## Getting Started

Follow these simple steps to get the HealthTracker application up and running on your local machine:

1. **Clone the repository:**
   ```shell
   git clone https://github.com/yourusername/healthtracker.git

2. **Build the application:**
   ```shell
   mvn clean install
3. **Run the application:**
   ```shell
   java -jar target/healthtracker-1.0.0.jar



   The application will be accessible at http://localhost:7000.

Usage
User Management
List Users: Use the API to list all users.
Add User: Use the API to add a new user.
Login: Minimal version of login with two roles. (Authorization for APIs not done)
Activity Tracker
Track Activities: Track the activities of each user.
Levels: Upgrade levels when a person burns more than 500 calories in a single activity.
Biometric Tracker
Calculate BMI: Use the provided BMI calculation endpoint.
Goals
People can set and update their goals on the go.
Contact
If you have any questions, issues, or feedback, feel free to reach out to us:

GitHub: [https://github.com/ashika-hussain](https://github.com/ashika-hussain)
