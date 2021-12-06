# Android CryptoCurrency Application

The purpose of this application is to monitorize and visually represent cryptocurrency changes.

## Prerequisites

* **[Latest version of Android Studio](https://developer.android.com/studio)**
* **[Android Phone or AVD](https://developer.android.com/studio/run/managing-avds)**
* **[Java JDK](https://www.oracle.com/java/technologies/javase-jdk16-downloads.html)**

## Note

    Currently the application is in develop stage and it is tested only on Pixel 2 XL Emulator with API 29.
    In case of any other AVD's or Smartphones the application (maybe) won't work properly.
 
## Installation
* Clone the repository into your local machine
* Open the project using Android Studio
* Build the project, after the build is completed the application will be installed on your avd/smartphone
 
## Application details9

The application is built around two APIs.
* **[CoinGekko v3 API](https://www.coingecko.com/api/documentations/v3)**
* **[CoinRanking v2 API](https://developers.coinranking.com/api/documentation)**

The applications gives possibility to track, analyze and save the latest cryptocurrencies.
After signing in, the user is able to monitorize every single cryptocurrency.
Selecting the preferred cryptocurrency, the user gets a detailed information about the currency
  * Price history divided into four part (1 day, 7 day, 1 year, 6 year)
  * Current price in USD
  * Current market cap, 24h volume change and current volume
  * Coin rank, all time high and a sort description

On the other side, the application offers possibility to analyze crypto's exchanges and
follow the latest crypto events
  
## Future updates

  * Implement a cryptocurrency converter to efficiently convert between currency values
  * User's portofolio & wallet
  * Price reminder for cryptocurrencies
  * Option to change between dark and light theme (currently dark is implemented)
  * Biometric authentication

## Used libraries and technologies

* **[Retrofit](https://square.github.io/retrofit/)**
* **[Single activity architecture using Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started)**
* **[Reactive UIs using LiveData](https://developer.android.com/topic/libraries/architecture/livedata)**
* **[Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)**
* **[Material Design](https://material.io/develop/android)**

The application is built around the **[MVVM](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel)** design principle.
 
## Screenshots
<img src="https://github.com/barabasizsolt/CryptoCurrencyApp/blob/master/screenshots/login.png" width="200" height="400">   <img src="https://github.com/barabasizsolt/CryptoCurrencyApp/blob/master/screenshots/signUp.png" width="200" height="400">   <img src="https://github.com/barabasizsolt/CryptoCurrencyApp/blob/master/screenshots/currencies.png" width="200" height="400">   <img src="https://github.com/barabasizsolt/CryptoCurrencyApp/blob/master/screenshots/currenciesTagFilter.png" width="200" height="400">  <img src="https://github.com/barabasizsolt/CryptoCurrencyApp/blob/master/screenshots/details1.png"  width="200" height="400">   <img src="https://github.com/barabasizsolt/CryptoCurrencyApp/blob/master/screenshots/details2.png" width="200" height="400">    <img src="https://github.com/barabasizsolt/CryptoCurrencyApp/blob/master/screenshots/navDrawer.png" width="200" height="400">    <img src="https://github.com/barabasizsolt/CryptoCurrencyApp/blob/master/screenshots/profile.png" width="200" height="400">    <img src="https://github.com/barabasizsolt/CryptoCurrencyApp/blob/master/screenshots/exchanges.png" width="200" height="400">   <img src="https://github.com/barabasizsolt/CryptoCurrencyApp/blob/master/screenshots/events.png" width="200" height="400">

