![alt text](https://github.com/Singularity-Coder/10000-Hours/blob/main/assets/logo192.png)
# 10000-Hours
Become the expert! This App is based on the "10,000 hour rule," popularized by Malcolm Gladwell in his book Outliers, suggests that achieving mastery in any skill requires approximately 10,000 hours of deliberate practice. The App allows you to log hours for every skill you want to master.

# Screenshots
![alt text](https://github.com/Singularity-Coder/10000-Hours/blob/main/assets/ss4.png)
![alt text](https://github.com/Singularity-Coder/10000-Hours/blob/main/assets/ss5.png)
![alt text](https://github.com/Singularity-Coder/10000-Hours/blob/main/assets/ss6.png)

## Tech stack & Open-source libraries
- Minimum SDK level 21
-  [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [LiveData](https://developer.android.com/topic/libraries/architecture/livedatahttps://developer.android.com/topic/libraries/architecture/livedata) for asynchronous.
- Jetpack
  - Lifecycle: Observe Android lifecycles and handle UI states upon the lifecycle changes.
  - ViewModel: Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
  - DataBinding: Binds UI components in your layouts to data sources in your app using a declarative format rather than programmatically.
  - Room: Constructs Database by providing an abstraction layer over SQLite to allow fluent database access.
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
- [Material-Components](https://github.com/material-components/material-components-android): Material design components for building ripple animation, and CardView.

## Architecture
![alt text](https://github.com/Singularity-Coder/10000-Hours/blob/main/assets/arch.png)

This App is based on the MVVM architecture and the Repository pattern, which follows the [Google's official architecture guidance](https://developer.android.com/topic/architecture).

The overall architecture of this App is composed of two layers; the UI layer and the data layer. Each layer has dedicated components and they have each different responsibilities.