package com.weatherapp.ui;

import com.weatherapp.model.WeatherData;
import com.weatherapp.service.WeatherService;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class WeatherDashboardFrame extends JFrame {
    private final WeatherService weatherService;

    private final JTextField cityField = new JTextField("Delhi");
    private final JComboBox<String> unitSelector = new JComboBox<>(new String[] {"Celsius", "Fahrenheit"});
    private final JButton searchButton = new JButton("Search");
    private final JButton refreshButton = new JButton("Refresh");
    private final JButton clearButton = new JButton("Clear");

    private final JLabel statusLabel = new JLabel("Ready");
    private final JLabel cityLabel = new JLabel("Delhi");
    private final JLabel conditionLabel = new JLabel("Clear sky");
    private final JLabel temperatureLabel = new JLabel("--");
    private final JLabel detailsLabel = new JLabel("Humidity: -- | Wind: --");
    private final JLabel metaLabel = new JLabel("Feels like -- | Updated --");
    private final JLabel emojiLabel = new JLabel("☀️");

    private boolean useCelsius = true;

    public WeatherDashboardFrame(WeatherService weatherService) {
        this.weatherService = weatherService;
        configureFrame();
        buildUI();
        attachListeners();
        fetchWeather("Delhi");
    }

    private void configureFrame() {
        setTitle("Weather Dashboard");
        setSize(520, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(242, 246, 255));
    }

    private void buildUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(16, 16));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        mainPanel.setBackground(new Color(242, 246, 255));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        topBar.setOpaque(false);
        cityField.setColumns(16);
        unitSelector.setSelectedIndex(0);

        topBar.add(cityField);
        topBar.add(unitSelector);
        topBar.add(searchButton);
        topBar.add(refreshButton);
        topBar.add(clearButton);

        JPanel weatherCard = new JPanel(new BorderLayout(10, 10));
        weatherCard.setBackground(new Color(44, 62, 102));
        weatherCard.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        cityLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        cityLabel.setForeground(Color.WHITE);
        conditionLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        conditionLabel.setForeground(new Color(220, 231, 255));
        temperatureLabel.setFont(new Font("SansSerif", Font.BOLD, 52));
        temperatureLabel.setForeground(Color.WHITE);
        detailsLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        detailsLabel.setForeground(new Color(226, 232, 255));
        metaLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        metaLabel.setForeground(new Color(199, 214, 255));
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        statusLabel.setForeground(new Color(195, 247, 214));

        JPanel summaryPanel = new JPanel();
        summaryPanel.setOpaque(false);
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.add(cityLabel);
        summaryPanel.add(conditionLabel);
        summaryPanel.add(Box.createVerticalStrut(12));
        summaryPanel.add(temperatureLabel);
        summaryPanel.add(detailsLabel);
        summaryPanel.add(metaLabel);

        emojiLabel.setFont(new Font("SansSerif", Font.PLAIN, 72));
        emojiLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setOpaque(false);
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(emojiLabel, BorderLayout.EAST);

        weatherCard.add(summaryPanel, BorderLayout.CENTER);
        weatherCard.add(statusPanel, BorderLayout.SOUTH);

        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(weatherCard, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void attachListeners() {
        searchButton.addActionListener(event -> fetchWeather(cityField.getText()));
        refreshButton.addActionListener(event -> fetchWeather(cityField.getText()));
        clearButton.addActionListener(event -> resetView());
        cityField.addActionListener(event -> fetchWeather(cityField.getText()));
        unitSelector.addActionListener(event -> {
            useCelsius = unitSelector.getSelectedIndex() == 0;
            String currentCity = cityField.getText();
            if (!currentCity.isBlank()) {
                fetchWeather(currentCity);
            }
        });
    }

    private void resetView() {
        cityField.setText("");
        cityLabel.setText("No city selected");
        conditionLabel.setText("Search a city to view weather");
        temperatureLabel.setText("--");
        detailsLabel.setText("Humidity: -- | Wind: --");
        metaLabel.setText("Feels like -- | Updated --");
        emojiLabel.setText("☀️");
        statusLabel.setText("Ready");
    }

    private void fetchWeather(String cityName) {
        String trimmedCity = normalizeCityName(cityName);
        if (trimmedCity.isEmpty()) {
            statusLabel.setText("Enter a valid city name");
            emojiLabel.setText("⚠️");
            return;
        }

        statusLabel.setText("Fetching...");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        SwingWorker<WeatherData, Void> worker = new SwingWorker<>() {
            @Override
            protected WeatherData doInBackground() throws Exception {
                return weatherService.fetchWeather(trimmedCity);
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                try {
                    WeatherData weather = get();
                    renderWeather(weather);
                } catch (Exception ex) {
                    statusLabel.setText("Unable to fetch weather");
                    cityLabel.setText(trimmedCity);
                    conditionLabel.setText("Connection error");
                    temperatureLabel.setText("--");
                    detailsLabel.setText("Humidity: -- | Wind: --");
                    metaLabel.setText("Please try again later");
                    emojiLabel.setText("⚠️");
                }
            }
        };

        worker.execute();
    }

    private void renderWeather(WeatherData weather) {
        String temperatureText = useCelsius
                ? String.format(Locale.US, "%.1f°C", weather.getTemperatureCelsius())
                : String.format(Locale.US, "%.1f°F", toFahrenheit(weather.getTemperatureCelsius()));

        String feelsLikeText = useCelsius
                ? String.format(Locale.US, "Feels like %.1f°C", weather.getFeelsLikeCelsius())
                : String.format(Locale.US, "Feels like %.1f°F", toFahrenheit(weather.getFeelsLikeCelsius()));

        cityLabel.setText(weather.getCity() + ", " + weather.getCountry());
        conditionLabel.setText(weather.getDescription());
        temperatureLabel.setText(temperatureText);
        detailsLabel.setText("Humidity: " + weather.getHumidity() + "% | Wind: " + weather.getWindSpeed() + " m/s");
        metaLabel.setText(feelsLikeText + " | Updated " + weather.getUpdatedAt());
        emojiLabel.setText(weatherService.getWeatherIcon(weather.getDescription()));
        statusLabel.setText("Live data");
    }

    private String normalizeCityName(String cityName) {
        if (cityName == null) {
            return "";
        }

        String normalized = cityName.trim();
        if (normalized.isEmpty()) {
            return "";
        }

        return normalized.replaceAll("\\s+", " ");
    }

    private double toFahrenheit(double celsius) {
        return (celsius * 9.0 / 5.0) + 32.0;
    }
}
