package com.weatherapp.service;

import com.weatherapp.model.WeatherData;

public interface WeatherService {
    WeatherData fetchWeather(String city) throws Exception;

    default String getWeatherIcon(String description) {
        if (description == null || description.isBlank()) {
            return "☀️";
        }

        String normalized = description.toLowerCase();
        if (normalized.contains("rain") || normalized.contains("drizzle")) {
            return "🌧️";
        }
        if (normalized.contains("thunder") || normalized.contains("storm")) {
            return "⛈️";
        }
        if (normalized.contains("snow")) {
            return "❄️";
        }
        if (normalized.contains("cloud")) {
            return "☁️";
        }
        if (normalized.contains("mist") || normalized.contains("fog") || normalized.contains("haze")) {
            return "🌫️";
        }
        if (normalized.contains("clear")) {
            return "☀️";
        }
        return "🌤️";
    }
}
