
# Notepad
MVVM andrioid note taking app


![GitHub](https://img.shields.io/github/license/Lamun365/Notepad?color=blue)
![Twitter URL](https://img.shields.io/twitter/url?label=follow&style=social&url=https://twitter.com/jamesi_lemon333?s=09)

**Notepad** is a open source note taking app that follows MVVM architectural design pattern 

***You can Install and test latest Notepad app from below 👇***

[![Notepad App](https://img.shields.io/badge/Notepad✅-APK-red.svg?style=for-the-badge&logo=android)](https://github.com/Lamun365/Notepad/releases/download/v1.0/app-release.apk)

## ⚙️ Features
* Create simple notes or checklist (todo) list
* Sort notes by date crated and title
* Dark Theme
* Quick search to find all notes
* Gird View
* Auto-saved

## 🚀 Technology Used

* Notepad built using Kotlin
* Room Database

## 📸 Screenshots

|||
|:--------------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------------:|
| ![Screenshot_20210517-204813_Notepad](https://user-images.githubusercontent.com/77043627/118533182-92f65b80-b769-11eb-9ca4-34487455945a.jpg) | ![Screenshot_20210517-205110_Notepad](https://user-images.githubusercontent.com/77043627/118533254-aa354900-b769-11eb-9e82-f07399908258.jpg) |
| ![Screenshot_20210517-210126_Notepad](https://user-images.githubusercontent.com/77043627/118533295-b8836500-b769-11eb-9010-24232b449473.jpg) | ![Screenshot_20210517-210215_Notepad](https://user-images.githubusercontent.com/77043627/118533362-cf29bc00-b769-11eb-9ae1-dd8349d578bb.jpg) |

## Built With 🛠
- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Data objects that notify views when the underlying database changes.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes. 
  - [ViewBinding](https://developer.android.com/topic/libraries/view-binding) - Generates a binding class for each XML layout file present in that module and allows you to more easily write code that interacts with views.
  - [Room](https://developer.android.com/training/data-storage/room) - Room is a database layer on top of a SQLite database. Room takes care of mundane tasks that you used to handle with a SQLiteOpenHelper.
- [Dependency Injection](https://developer.android.com/training/dependency-injection) - 
  - [Hilt-Dagger](https://dagger.dev/hilt/) - Standard library to incorporate Dagger dependency injection into an Android application.
  - [Hilt-ViewModel](https://developer.android.com/training/dependency-injection/hilt-jetpack) - DI for injecting `ViewModel`.
- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) - Jetpack DataStore is a data storage solution that allows you to store key-value pairs or typed objects with protocol buffers. DataStore uses Kotlin coroutines and Flow to store data asynchronously, consistently, and transactionally.
- [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android.

# Package Structure
    
    com.lexo.notepad        # Root Package
    .
    ├── db                # For data handling.
    |
    ├── di                  # Dependency Injection             
    |
    ├── ui                  # Activity/View layer
    │   ├── viewmodel       # ViewModels
    │   └── adapter         # Adpaters
    │   └── fragment        # Fragnents
    |
    └── util               # Utility Classes / Kotlin extensions


## Architecture
This app uses [***MVVM (Model View View-Model)***](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) architecture.

![](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)


## License
```
MIT License

Copyright (c) 2021 Lamun

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
