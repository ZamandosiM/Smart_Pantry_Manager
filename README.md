Smart Pantry Manager

Project Description

Smart Pantry Manager is an Android application developed to help users manage ingredients in their pantry and discover recipes they can prepare using available ingredients.

Features

- Add ingredients with quantities and measurement units.
- View all saved pantry ingredients.
- Edit existing ingredients.
- Delete ingredients.
- Display suggested recipes based on available ingredients and sufficient quantities.
- View recipe ingredients and cooking instructions.
- Save preferred measurement unit settings.
- Store pantry information using SQLite.

Technologies Used

- Java
- Android Studio
- XML layouts
- SQLite Database
- Git and GitHub

How to Run the Application

1. Download or clone the GitHub repository.
2. Open the project in Android Studio.
3. Allow Gradle to finish syncing.
4. Connect an Android phone with USB debugging enabled or start an Android emulator.
5. Click Run to install and launch the application.

How to Use

1. Open Smart Pantry Manager.
2. Select Add Ingredient to enter pantry items.
3. Add each ingredient's name, quantity and unit.
4. Select Suggested Recipes to view recipes that match your available ingredients.
5. Tap a recipe to view its ingredients and preparation instructions.
6. Use Settings to save your preferred measurement unit.

Developer

Developed as part of a Mobile App Development academic project.

Technical Implementation

Database Management

Smart Pantry Manager uses SQLite to store pantry ingredients locally on the Android device. Each ingredient contains a name, quantity, and measurement unit. Users can add, view, update, and delete ingredients.

Recipe Suggestion Logic

The application compares the ingredients and quantities stored in the pantry with the requirements of predefined recipes. Only recipes that can be prepared using the available ingredients and sufficient quantities are displayed.

Input Validation

The application validates ingredient information before saving it. Quantities must be greater than zero to prevent invalid pantry records.

User Preferences

The Settings screen allows users to save their preferred measurement unit using Android SharedPreferences.