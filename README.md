# User App

This app is designed to display a list of users with the ability to check if a sentence is a palindrome and navigate between screens.


## Setup Instructions

### Prerequisites
- Android Studio
- Android SDK
- Internet connection (to access the API)

### Installation

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/username/repository.git
   

1. **Open the Project in Android Studio:**
   - Open Android Studio.
   - Select "Open an existing Android Studio project".
   - Navigate to the cloned project folder and click "OK".

2. **Sync Gradle:**
   - Click "Sync Now" if there are changes in `build.gradle`.

3. **Run the App:**
   - Select a device or emulator to run the app on.
   - Click "Run" (or press Shift + F10) to start the application.

## Project Structure

- **First Screen (`FirstScreenActivity.kt`):**
  - Handles user input and palindrome logic.
  - Manages navigation buttons.

- **Second Screen (`SecondScreenActivity.kt`):**
  - Displays user name data.
  - Manages the button to navigate to the third screen.

- **Third Screen (`ThirdScreenActivity.kt`):**
  - Displays a list of users with pagination and pull-to-refresh functionality.
  - Manages item clicks to update the second screen.

## API Reference

- **API Server:** `https://reqres.in/api/`





#### Get User

```http
 GET https://reqres.in/api/users
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `page` | `int` | Page number for pagination.|
| `per_page` | `int` | Number of items per page. |




## APK Distribution
If you prefer to install the app directly without building it yourself, you can download the latest APK from the following link: Documentation

[Download APK](https://drive.google.com/drive/folders/1qcJsKVRRqWGYV2rczxD-BrRZzsBmn4xS?usp=sharing)


## 🔗 Links
[![portfolio](https://img.shields.io/badge/my_portfolio-000?style=for-the-badge&logo=ko-fi&logoColor=white)](https://hildatulwardah.vercel.app/)
[![linkedin](https://img.shields.io/badge/linkedin-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/hildatul-wardah-522856238/)


