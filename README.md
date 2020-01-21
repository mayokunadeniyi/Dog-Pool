# Dog Pool [![BCH compliance](https://bettercodehub.com/edge/badge/mayokunthefirst/Dog-Pool?branch=master)](https://bettercodehub.com/)


## Introduction
An Android sample application implemented using MVVM pattern, Retrofit2, LiveData, ViewModel, Coroutines, Room, Rx-Java, Navigation Components and Data Binding.

<p align="center"><a><img src="https://github.com/mayokunthefirst/Dog-Pool/blob/master/gif/demo.gif" width="300"></a></p>

## Architecture
The architecture of this application relies and complies with the following points below:
* A single-activity architecture, using the [Navigation Components](https://developer.android.com/guide/navigation) to manage fragment operations.
* Pattern [Model-View-ViewModel](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel)(MVVM) which facilitates a separation of development of the graphical user interface.
* [Android architecture components](https://developer.android.com/topic/libraries/architecture/) which help to keep the application robust, testable, and maintainable.

## Technologies used:

* [Retrofit](https://square.github.io/retrofit/) a REST Client for Android which makes it relatively easy to retrieve and upload JSON (or other structured data) via a REST based webservice.
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) to store and manage UI-related data in a lifecycle conscious way.
* [RxJava](https://github.com/ReactiveX/RxJava) used in the the network section of the project to perform network calls asynchronously. RxJava helps in composing asynchronous and event-based programs using observable sequences.
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) to handle data in a lifecycle-aware fashion.
* [Navigation Component](https://developer.android.com/guide/navigation) to handle all navigations and also passing of data between destinations.
* [Timber](https://github.com/JakeWharton/timber) - a logger with a small, extensible API which provides utility on top of Android's normal Log class.
* [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) used to manage the local storage i.e. `writing to and reading from the database`. Coroutines help in managing background threads and reduces the need for callbacks.
* [Data Binding](https://developer.android.com/topic/libraries/data-binding/) to declaratively bind UI components in layouts to data sources.
* [Room](https://developer.android.com/topic/libraries/architecture/room) persistence library which provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.

## Installation
Dog Pool requires a minimum API level of 23. Clone the repository and run. 

## Contributions
All contributions are welcome! Simply make a PR.
