# SkyLook

A simple and elegant weather app for Android that provides current weather conditions, hourly forecasts, and 5-day forecasts using data from Open-Meteo.

## Features

- **Current Weather**: Temperature, feels like, humidity, wind speed, precipitation, and day/night status
- **Hourly Forecast**: 24-hour forecast with temperature, weather conditions, humidity, and precipitation probability
- **5-Day Forecast**: Daily weather with max/min temperatures, sunrise/sunset times, and precipitation probability
- **City Search**: Search and select cities worldwide using Open-Meteo Geocoding API
- **Weather Icons**: Visual weather condition icons for easy reading
- **Material Design**: Clean UI following Material Design 3 guidelines

## Screenshots

## Tech Stack

- **Language**: Java
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 37
- **UI**: Material Design 3 components
- **Architecture**: MVC with SQLite database
- **Networking**: HttpURLConnection with Open-Meteo API
- **Build**: Gradle with Kotlin DSL

## API

This app uses the [Open-Meteo](https://open-meteo.com/) API for weather data and geocoding:

- Geocoding API: https://geocoding-api.open-meteo.com/v1/search
- Forecast API: https://api.open-meteo.com/v1/forecast

No API key is required.

## Installation

### Prerequisites

- Android Studio Arctic Fox or newer
- Android SDK 24 or higher
- Gradle 8.0+

### Build from Source

1. Clone the repository:
   ```bash
   git clone https://github.com/thisis000/SkyLook.git
   ```

2. Open the project in Android Studio

3. Sync Gradle dependencies

4. Build the project:
   ```bash
   ./gradlew assembleDebug
   ```

5. Install on device/emulator:
   ```bash
   ./gradlew installDebug
   ```

Or download the APK from the Releases section.

## Project Structure

```
app/
├── src/main/
│   ├── java/com/arif/skylook/
│   │   ├── MainActivity.java          # Main weather display
│   │   ├── CitySelectionActivity.java # City search and selection
│   │   ├── SplashActivity.java        # App entry point
│   │   ├── ApiClient.java             # API calls to Open-Meteo
│   │   ├── DatabaseHelper.java        # SQLite for city storage
│   │   ├── CityAdapter.java           # RecyclerView adapter
│   │   ├── WeatherIconHelper.java     # Weather code to icon mapping
│   │   ├── City.java                  # City model
│   │   ├── CurrentWeather.java        # Current weather model
│   │   ├── HourlyWeather.java         # Hourly forecast model
│   │   └── DailyWeather.java          # Daily forecast model
│   └── res/
│       ├── drawable/                  # Weather icons
│       ├── layout/                    # UI layouts
│       └── values/                    # Colors, themes, strings
```

## Usage

1. Launch the app
2. On first launch, search and select a city
3. View current weather conditions
4. Scroll horizontally to see hourly forecast
5. Scroll down to see 5-day forecast
6. Tap the edit icon in the header to change city

## Permissions

- **Internet**: Required to fetch weather data from Open-Meteo API

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Weather data provided by [Open-Meteo](https://open-meteo.com/)
- Icons inspired by Material Design
- Built with [Material Components for Android](https://github.com/material-components/material-components-android)

## Contact

For questions or suggestions, please open an issue on GitHub.

---

Made with care for weather enthusiasts