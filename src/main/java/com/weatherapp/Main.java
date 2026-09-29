package com.weatherapp;

import com.weatherapp.service.OpenWeatherMapService;
import com.weatherapp.service.WeatherService;
import com.weatherapp.ui.WeatherDashboardFrame;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            if (GraphicsEnvironment.isHeadless()) {
                System.err.println("This application requires a graphical environment.");
                return;
            }

            WeatherService weatherService = new OpenWeatherMapService();
            WeatherDashboardFrame frame = new WeatherDashboardFrame(weatherService);
            frame.setVisible(true);
        });
    }
}
