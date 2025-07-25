# Homework3 - Weather Map Android App

A Kotlin-based Android application that provides real-time weather information with interactive map integration. Users can search for weather conditions in any city worldwide and view the location on Google Maps.

## Features

- **City Weather Search**: Enter any city name to get current weather information
- **Real-time Temperature Display**: Shows temperature in Celsius with weather icons
- **Interactive Google Maps**: Visual location display with camera animation
- **Weather Icons**: Dynamic weather condition icons from OpenWeatherMap
- **Network Connectivity Check**: Handles offline scenarios gracefully
- **Intuitive UI**: Clean, user-friendly interface with Material Design

## Screenshots

The app displays:
- City search input field (defaults to "Boston, US")
- Current temperature and weather icon
- Interactive Google Maps view
- Real-time weather updates

## Technologies Used

- **Language**: Kotlin
- **Platform**: Android (API 27-35)
- **Architecture**: Android Architecture Components
- **Maps**: Google Maps Android API
- **Weather Data**: OpenWeatherMap API
- **Async Operations**: Kotlin Coroutines
- **UI Framework**: AndroidX with Material Design
- **Build System**: Gradle with Kotlin DSL

## Prerequisites

### API Keys Required

1. **OpenWeatherMap API Key**
   - Sign up at [OpenWeatherMap](https://openweathermap.org/api)
   - Get your free API key
   - Note: The current implementation includes a demo API key

2. **Google Maps API Key**
   - Enable Google Maps SDK for Android in [Google Cloud Console](https://console.cloud.google.com/)
   - Create credentials and get your API key
   - Note: The current implementation includes a demo API key

### Development Environment

- **Android Studio**: Arctic Fox or later (required for Android development)
- **Android SDK**: API 27-35 (automatically managed by Android Studio)
- **Minimum SDK**: API 27 (Android 8.1)
- **Target SDK**: API 35
- **Kotlin**: 1.8+
- **JDK**: 8+ (included with Android Studio)
- **Gradle**: 8.11.1 (wrapper included in project)

## Installation & Setup

⚠️ **Important**: This is an Android project that requires Android Studio and the Android SDK to build and run.

1. **Clone the Repository**
   ```bash
   git clone https://github.com/li1345825138/Homework3.git
   cd Homework3
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory and select it

3. **Configure API Keys** (Optional - Demo keys are included)
   
   **For OpenWeatherMap API:**
   - Open `MainActivity.kt`
   - Replace the `apikey` value with your API key:
     ```kotlin
     private val apikey = "&appid=YOUR_OPENWEATHERMAP_API_KEY"
     ```
   
   **For Google Maps API:**
   - Open `app/src/main/AndroidManifest.xml`
   - Replace the API key in the meta-data tag:
     ```xml
     <meta-data android:name="com.google.android.maps.v2.API_KEY"
         android:value="YOUR_GOOGLE_MAPS_API_KEY" />
     ```

4. **Build and Run**
   - Sync the project with Gradle files
   - Wait for Android Studio to download dependencies
   - Connect an Android device or start an emulator
   - Run the app (Shift+F10 or click the Run button)

## Usage

1. **Launch the App**: Open the Weather Map app on your device
2. **Search for a City**: 
   - Tap the text field (shows "Boston,us" by default)
   - Enter any city name (e.g., "New York", "London", "Tokyo")
   - Press the "OK" button or hit Enter
3. **View Results**:
   - Temperature displays in Celsius
   - Weather icon shows current conditions
   - Map animates to show the city location
4. **Explore Different Cities**: Repeat the search process for other locations

## Project Structure

```
app/
├── src/main/
│   ├── java/edu/umb/cs443/
│   │   └── MainActivity.kt          # Main application logic
│   ├── res/
│   │   ├── layout/
│   │   │   └── activity_main.xml    # UI layout
│   │   ├── values/
│   │   │   └── strings.xml          # String resources
│   │   └── ...
│   └── AndroidManifest.xml          # App configuration
├── build.gradle.kts                 # Module dependencies
└── ...
```

## Key Components

### MainActivity.kt
- **Weather API Integration**: Fetches data from OpenWeatherMap
- **Geocoding**: Converts city names to coordinates
- **Maps Integration**: Controls Google Maps display
- **UI Management**: Updates temperature display and weather icons
- **Network Handling**: Checks connectivity and manages API calls

### Core Functions
- `getWeatherInfo()`: Main function to fetch weather data
- `processWeatherJson()`: Parses weather API response
- `setWeatherIcon()`: Downloads and displays weather icons
- `downloadUrl()`: Handles HTTP requests to APIs

## APIs Used

### OpenWeatherMap API
- **Geocoding API**: `https://api.openweathermap.org/geo/1.0/direct`
- **Current Weather API**: `https://api.openweathermap.org/data/3.0/onecall`
- **Weather Icons**: `https://openweathermap.org/img/wn/[icon]@2x.png`

### Google Maps API
- **Maps SDK for Android**: Provides interactive map functionality

## Network Requirements

- **Internet Connection**: Required for API calls (WiFi or Cellular)
- **Permissions**: 
  - `INTERNET` permission in AndroidManifest.xml

## Error Handling

- Network connectivity validation
- JSON parsing error handling
- API response error management
- Graceful fallback for failed requests

## Course Information

This project was developed as part of:
- **Course**: CS443 - Mobile Application Development
- **Institution**: University of Massachusetts Boston (UMB)
- **Assignment**: Homework 3

## License

This project is for educational purposes as part of coursework requirements.

## Contributing

This is a homework assignment. Please respect academic integrity policies.

## Troubleshooting

### Common Issues

1. **Maps not loading**: Check Google Maps API key configuration
2. **Weather data not updating**: Verify OpenWeatherMap API key and network connection
3. **App crashes on startup**: Ensure all dependencies are properly synced

### Debug Information

The app uses logging with tag `edu.umb.cs443.MYMSG` for debugging purposes.

## Future Enhancements

Potential improvements could include:
- Extended weather forecast (hourly/daily)
- Weather alerts and notifications
- Multiple location bookmarks
- Temperature unit conversion (Celsius/Fahrenheit)
- Dark mode support
- Offline data caching