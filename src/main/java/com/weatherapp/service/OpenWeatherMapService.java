package com.weatherapp.service;

import com.weatherapp.model.WeatherData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class OpenWeatherMapService implements WeatherService {
    private static final String DEMO_CITY = "Delhi";
    private static final String OPEN_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Override
    public WeatherData fetchWeather(String city) throws Exception {
        String safeCity = city == null ? DEMO_CITY : city.trim();
        if (safeCity.isEmpty()) {
            safeCity = DEMO_CITY;
        }

        String apiKey = System.getenv("OPENWEATHER_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            return buildDemoWeather(safeCity);
        }

        String encodedCity = URLEncoder.encode(safeCity, StandardCharsets.UTF_8);
        String requestUrl = OPEN_WEATHER_URL + "?q=" + encodedCity + "&appid=" + apiKey + "&units=metric";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Weather service returned status " + response.statusCode());
        }

        return parseWeatherResponse(response.body(), safeCity);
    }

    public WeatherData parseWeatherResponse(String responseBody, String fallbackCity) {
        JSONObject root = new JSONObject(responseBody);
        JSONObject main = root.optJSONObject("main");
        JSONObject wind = root.optJSONObject("wind");
        JSONArray weatherArray = root.optJSONArray("weather");

        double temperature = main != null ? main.optDouble("temp", 25.0) : 25.0;
        double feelsLike = main != null ? main.optDouble("feels_like", temperature) : temperature;
        int humidity = main != null ? main.optInt("humidity", 50) : 50;
        double windSpeed = wind != null ? wind.optDouble("speed", 4.0) : 4.0;

        String description = "Clear sky";
        if (weatherArray != null && !weatherArray.isEmpty()) {
            description = weatherArray.getJSONObject(0).optString("description", "Clear sky");
        }

        String city = root.optString("name", fallbackCity);
        String country = root.optJSONObject("sys") != null
                ? root.getJSONObject("sys").optString("country", "IN")
                : "IN";

        String updatedAt = formatUpdatedAt(root.optLong("dt", System.currentTimeMillis() / 1000L));
        return new WeatherData(city, country, temperature, feelsLike, humidity, windSpeed, description, updatedAt);
    }

    @Override
    public String getWeatherIcon(String description) {
        if (description == null || description.isBlank()) {
            return "☀️";
        }

        String normalized = description.toLowerCase(Locale.ROOT);
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

    private WeatherData buildDemoWeather(String city) {
        return switch (city.toLowerCase()) {
            case "mumbai" -> new WeatherData("Mumbai", "IN", 28.0, 30.0, 72, 8.1, "Light rain", "Updated now");
            case "bengaluru", "bangalore" -> new WeatherData("Bengaluru", "IN", 25.0, 26.0, 68, 5.4, "Partly cloudy", "Updated now");
            case "london" -> new WeatherData("London", "UK", 18.0, 17.0, 64, 12.2, "Mild breeze", "Updated now");
            default -> new WeatherData(city, "IN", 30.2, 32.1, 55, 6.4, "Clear sky", "Updated now");
        };
    }

    private String formatUpdatedAt(long epochSeconds) {
        return Instant.ofEpochSecond(epochSeconds)
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("MMM d, HH:mm"));
    }
}
