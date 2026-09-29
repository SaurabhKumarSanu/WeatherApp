const cityWeather = {
  Delhi: {
    country: 'India',
    temperature: 30,
    feelsLike: 32,
    humidity: 55,
    wind: 6.4,
    visibility: 8.5,
    pressure: 1012,
    description: 'Clear sky',
    icon: '☀️',
    forecast: [
      { day: 'Mon', temp: 31, icon: '🌤️' },
      { day: 'Tue', temp: 29, icon: '⛅' },
      { day: 'Wed', temp: 33, icon: '🌞' }
    ]
  },
  Mumbai: {
    country: 'India',
    temperature: 28,
    feelsLike: 30,
    humidity: 72,
    wind: 8.1,
    visibility: 6.2,
    pressure: 1008,
    description: 'Light rain',
    icon: '🌦️',
    forecast: [
      { day: 'Mon', temp: 27, icon: '🌧️' },
      { day: 'Tue', temp: 28, icon: '🌤️' },
      { day: 'Wed', temp: 29, icon: '⛅' }
    ]
  },
  Bengaluru: {
    country: 'India',
    temperature: 25,
    feelsLike: 26,
    humidity: 68,
    wind: 5.4,
    visibility: 9.1,
    pressure: 1014,
    description: 'Partly cloudy',
    icon: '⛅',
    forecast: [
      { day: 'Mon', temp: 24, icon: '🌤️' },
      { day: 'Tue', temp: 25, icon: '⛅' },
      { day: 'Wed', temp: 26, icon: '🌦️' }
    ]
  },
  London: {
    country: 'United Kingdom',
    temperature: 18,
    feelsLike: 17,
    humidity: 64,
    wind: 12.2,
    visibility: 10.4,
    pressure: 1016,
    description: 'Mild breeze',
    icon: '🌤️',
    forecast: [
      { day: 'Mon', temp: 17, icon: '⛅' },
      { day: 'Tue', temp: 18, icon: '🌧️' },
      { day: 'Wed', temp: 19, icon: '🌤️' }
    ]
  }
};

const state = {
  city: 'Delhi',
  unit: 'C'
};

const searchInput = document.getElementById('searchInput');
const searchForm = document.getElementById('searchForm');
const unitButtons = [...document.querySelectorAll('.unit-btn')];

function formatTemp(value) {
  return state.unit === 'C' ? `${Math.round(value)}°C` : `${Math.round((value * 9) / 5 + 32)}°F`;
}

function renderWeather(cityName) {
  const city = cityWeather[cityName] || cityWeather.Delhi;
  state.city = cityName;

  document.getElementById('cityName').textContent = `${cityName}, ${city.country}`;
  document.getElementById('weatherDescription').textContent = city.description;
  document.getElementById('weatherIcon').textContent = city.icon;
  document.getElementById('temperature').textContent = formatTemp(city.temperature);
  document.getElementById('feelsLike').textContent = `Feels like ${formatTemp(city.feelsLike)}`;
  document.getElementById('humidity').textContent = `${city.humidity}%`;
  document.getElementById('wind').textContent = `${city.wind.toFixed(1)} km/h`;
  document.getElementById('visibility').textContent = `${city.visibility.toFixed(1)} km`;
  document.getElementById('pressure').textContent = `${city.pressure} hPa`;

  const forecastContainer = document.getElementById('forecast');
  forecastContainer.innerHTML = city.forecast
    .map((item) => `
      <article class="forecast-card">
        <span class="forecast-day">${item.day}</span>
        <span class="forecast-icon">${item.icon}</span>
        <strong>${formatTemp(item.temp)}</strong>
      </article>
    `)
    .join('');
}

searchForm.addEventListener('submit', (event) => {
  event.preventDefault();
  const cityName = searchInput.value.trim();
  if (!cityName) {
    return;
  }

  const lookupKey = cityName.charAt(0).toUpperCase() + cityName.slice(1).toLowerCase();
  const selectedKey = Object.keys(cityWeather).find((name) => name.toLowerCase() === lookupKey.toLowerCase());

  renderWeather(selectedKey || lookupKey);
});

unitButtons.forEach((button) => {
  button.addEventListener('click', () => {
    unitButtons.forEach((btn) => btn.classList.toggle('active', btn === button));
    state.unit = button.dataset.unit;
    renderWeather(state.city);
  });
});

searchInput.value = state.city;
renderWeather(state.city);
   