<p align="center">  
This Simple e-commerce App demonstrates modern Android development with Hilt, Coroutines, Flow, Jetpack libraries, Retrofit and Material Design based on MVVM architecture.

<p align="center">
</p>


## Tech stack & Open-source libraries
- Minimum SDK level 24
- [Kotlin](https://kotlinlang.org/) based, StateFlow, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- Jetpack
  - Lifecycle: Observe Android lifecycles and handle UI states upon the lifecycle changes.
  - ViewModel: Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
  - Stateflow: An observable hot flow for emitting the latest data.
  - Kotlin Flow: A cold flow triggered by the collecting functions.
  - [Hilt](https://dagger.dev/hilt/): for dependency injection.
  - XML for UI design layout.
- Architecture
  - MVVM Architecture (View - ViewModel - Model)
- [ksp](https://github.com/google/ksp): Kotlin Symbol Processing API.
- [Retrofit](https://github.com/square/retrofit): A networking library for fetching data over the internet
- [Turbine](https://github.com/cashapp/turbine): A small testing library for kotlinx.coroutines Flow.
- Material3 design guidelines
- Firebase for user authentication and email verification.
  
## Architecture
This app is based on the MVVM architecture and the Repository pattern, which follows the [Google's official architecture guidance](https://developer.android.com/topic/architecture).

The overall architecture here is composed of three layers; the presentation layer, the domain layer, the data layer according to the Clean Architecture paradigm, with another for navigation. Each layer has dedicated components and they have each different responsibilities, as defined below:

### Presentation Layer
The presentation layer consists of the ui components as well as the view models to control the lifecyle of the application.

### Data Layer
The data Layer consists of repositories, which include business logic, such as querying data from the remote data source. It follows the [single source of truth](https://en.wikipedia.org/wiki/Single_source_of_truth) principle.<br>

### Domain
The domain layer consists mainly of repostory interfaces, use cases and business logic as followed by the clean architecure principles.


## Prerequisites
To build this project, you require:

- Android Studio koala
- Gradle or AGP 8.5.2
- Kotlin version 2.0.0,
- Clone the repository.
- Get your personalized paystack SECRET TEST TOKEN from your paystack profile and set the value as 'bearer-token' in your local.properties file and rebuild your project.
## How to Get your secret key and public test keys:
- Follow this article
- Assign the value to bearer token like this [paysack api keys](https://support.paystack.com/hc/en-us/articles/360009881600-Paystack-Test-Keys-Live-Keys-and-Webhooks): Paystack Public and Secret Keys:
  public-key=hhhhhhhhh.
  secret-key=ssssss
- N-B: do not use string quotes for the token, just paste it directly in the profile as instructed, and build the app.

