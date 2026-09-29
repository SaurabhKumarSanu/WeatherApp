# Weather Dashboard

A clean, production-style weather dashboard project with a proper Java package structure and a separate browser demo.

## Project structure

```text
weather-dashboard/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── weatherapp/
│                   ├── Main.java
│                   ├── model/
│                   ├── service/
│                   └── ui/
├── web/
│   ├── index.html
│   ├── style.css
│   └── script.js
├── legacy/
│   └── archived experiments and early prototypes
├── pom.xml
├── README.md
└── .gitignore
```

## Features

- Search weather by city
- Toggle between Celsius and Fahrenheit
- Responsive dashboard UI
- Demo mode when no API key is configured
- Clear separation between app logic, models, services, and UI

## Run the Java app

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass=com.weatherapp.Main
```

If `OPENWEATHER_API_KEY` is not set, the app uses a demo dataset.

## Run the browser demo

Open the file in a browser:

```text
C:/saurabh java/web/index.html
```

## Environment

- Java 17
- Maven

## Notes

- The app uses the OpenWeatherMap API when an API key is set in the `OPENWEATHER_API_KEY` environment variable.
- The `legacy/` folder contains earlier scratch and learning files, kept separate from the main app.
