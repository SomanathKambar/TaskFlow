# TaskFlow – Smart Task Manager

TaskFlow is a production-grade offline-first Android application built with Kotlin and Jetpack Compose. It demonstrates clean architecture, reactive data flow, and modern Android development practices.

## 🚀 Features

- **CRUD Operations**: Create, Read, Update, and Delete tasks.
- **Offline-First**: Powered by Room for seamless offline use.
- **Reactive UI**: Uses Kotlin Flow and StateFlow for a responsive user experience.
- **Smart Search**: Real-time task filtering based on title and description.
- **Soft Delete & Trash**: Deleted tasks move to a Trash screen where they can be restored or permanently deleted.
- **Automated Cleanup**: A background WorkManager job permanently deletes items from the Trash after 7 days.
- **Clean Architecture**: Clear separation of concerns (Data, Domain, Presentation).
- **Dependency Injection**: Managed by Dagger Hilt.

## 🏗️ Architecture

TaskFlow follows the Clean Architecture pattern:

- **Presentation Layer**: Jetpack Compose UI with ViewModels observing StateFlow.
- **Domain Layer**: Pure business logic containing Models, Repositories (interfaces), and UseCases.
- **Data Layer**: Room Database implementation, DAOs, and Repositories (implementations).

## 🛠️ Tech Stack

- **Kotlin**: Primary language.
- **Jetpack Compose**: Modern toolkit for building native UI.
- **Room**: SQLite object mapping library.
- **Hilt**: Dependency injection.
- **WorkManager**: Background task scheduling.
- **Coroutines & Flow**: Asynchronous programming and reactive streams.
- **Navigation Compose**: Type-safe navigation between screens.

## 🧪 Testing

The project includes unit tests for the domain layer's UseCases using **JUnit**, **MockK**, and **Google Truth**.

## 🚦 Getting Started

1. Clone the repository.
2. Open the project in Android Studio (Giraffe or newer).
3. Build and run on an emulator or physical device (Min SDK: 24).
