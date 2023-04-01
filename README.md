# WearOS app to view Mars Rover photos

This is a WearOS app that displays random photos from the three Mars rovers *Curiosity*, *
Opportunity*,
and *Spirit* in a [tile](https://developer.android.com/training/wearables/tiles).

This project uses the [NASA API](https://api.nasa.gov/index.html#browseAPI) _Mars Rover Photos_.

## Structure

The project is structured into two different main folders:

* `tile` - contains all code to provide
  a [tile](https://developer.android.com/training/wearables/tiles), which displays images.
* `app` - contains all code for networking, business logic and UI for the
  supporting [WearOS app](https://developer.android.com/training/wearables/apps).

### Libraries

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Horologist](https://github.com/google/horologist)
- [Coil](https://github.com/coil-kt/coil)

- [Ktor client](https://ktor.io/docs/create-client.html)
- [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
- [Kotlinx datetime](https://github.com/Kotlin/kotlinx-datetime)

- [Android Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- [Android WorkManager](https://developer.android.com/guide/background/persistent)
- [Android DataStore](https://developer.android.com/topic/libraries/architecture/datastore)

- [mockk](https://mockk.io/)
- [Kotest](https://kotest.io/)