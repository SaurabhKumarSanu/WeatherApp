package com.weatherapp.model;

public class WeatherData {
    private final String city;
    private final String country;
    private final double temperatureCelsius;
    private final double feelsLikeCelsius;
    private final int humidity;
    private final double windSpeed;
    private final String description;
    private final String updatedAt;

    public WeatherData(String city, String country, double temperatureCelsius, double feelsLikeCelsius,
                       int humidity, double windSpeed, String description, String updatedAt) {
        this.city = city;
        this.country = country;
        this.temperatureCelsius = temperatureCelsius;
        this.feelsLikeCelsius = feelsLikeCelsius;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.description = description;
        this.updatedAt = updatedAt;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public double getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public double getFeelsLikeCelsius() {
        return feelsLikeCelsius;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getDescription() {
        return description;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
