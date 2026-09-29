package com.weatherapp.service;

import com.weatherapp.model.WeatherData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OpenWeatherMapServiceTest {

    @Test
    void parseWeatherResponse_shouldMapJsonFields() {
        String json = "{"
                + "\"name\":\"Delhi\","
                + "\"sys\":{\"country\":\"IN\"},"
                + "\"main\":{\"temp\":28.4,\"feels_like\":30.1,\"humidity\":65},"
                + "\"wind\":{\"speed\":4.5},"
                + "\"weather\":[{\"description\":\"clear sky\"}]"
                + "}";

        WeatherData weather = new OpenWeatherMapService().parseWeatherResponse(json, "Delhi");

        assertEquals("Delhi", weather.getCity());
        assertEquals("IN", weather.getCountry());
        assertEquals(28.4, weather.getTemperatureCelsius(), 0.0001);
        assertEquals(30.1, weather.getFeelsLikeCelsius(), 0.0001);
        assertEquals(65, weather.getHumidity());
        assertEquals(4.5, weather.getWindSpeed(), 0.0001);
        assertEquals("clear sky", weather.getDescription().toLowerCase());
    }

    @Test
    void getWeatherIcon_shouldMapCommonConditions() {
        OpenWeatherMapService service = new OpenWeatherMapService();

        assertEquals("☀️", service.getWeatherIcon("clear sky"));
        assertEquals("🌧️", service.getWeatherIcon("light rain"));
        assertEquals("☁️", service.getWeatherIcon("few clouds"));
        assertEquals("❄️", service.getWeatherIcon("snow"));
    }
}
