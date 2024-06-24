[![Android CI](https://github.com/mitko-donchev/MemoRise/actions/workflows/android.yml/badge.svg)](https://github.com/mitko-donchev/MemoRise/actions/workflows/android.yml)

# MemoRise

MemoRise is a cutting-edge Android note-taking application designed to provide a simple yet powerful way to manage your notes. Built with the latest Android technologies, it delivers a seamless and modern user experience.

## Features

- **Modern UI**: Follows the latest Material 3 design guidelines for a clean and intuitive interface.
- **Note Management**: Create, read, update, and delete notes with ease.
- **Smooth Transitions**: Utilizes Shared Element Transitions for a fluid navigation experience.
- **Robust Architecture**: Implements a multi-module structure with MVVM and MVI architectural patterns.
- **Dependency Injection**: Uses Hilt for efficient and modular dependency management.
- **Persistent Storage**: Integrates Room for local database storage.
- **Compatibility**: Supports the latest stable Android SDK and works with Java 17.

## Technologies Used

### Frontend
- **Jetpack Compose**: Modern toolkit for building native UI with Kotlin.
- **Material 3 Design**: Incorporates the latest Material Design principles for a visually appealing and user-friendly interface.

### Architecture
- **Multi-Module**: Ensures scalability and separation of concerns by dividing the app into distinct modules.
- **MVVM (Model-View-ViewModel)**: Promotes a clear separation between UI and business logic.
- **MVI (Model-View-Intent)**: Provides unidirectional data flow, enhancing state management.

### Backend
- **Room**: Provides an abstraction layer over SQLite to allow fluent database access.
- **Hilt**: Simplifies dependency injection by providing a standard way to integrate Dagger with Android applications.

## Getting Started

### Prerequisites
- **Android Studio**: Latest version recommended.
- **Java 17**: Ensure you have Java 17 installed on your development machine.

### Installation

1. **Clone the repository**:
   ```
   git clone https://github.com/mitko-donchev/MemoRise.git
   ```
2. Open the project in Android Studio.
3. Build the project:
   ``` 
   ./gradlew build 
   ```
4. Run the app on an emulator or connected device.

## Tech Stack

- **UI**: Jetpack Compose for a modern, declarative UI.
- **Dependency Injection**: Hilt for managing dependencies efficiently.
- **Database**: SQLite with Room.
- **Architecture**: Multi-module, MVVM, MVI.
- **Material Design**: Adhering to the latest Material Design guidelines for a seamless user
  experience.
- **Language**: Kotlin.

## Future Development

- **Integrate slide actions**
- **Enhance the design**
- **Develop more features such as sharing and saving**

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE.md) file for
details.
