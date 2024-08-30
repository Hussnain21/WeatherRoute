Weather and Maps Application
Overview
This Java application integrates two powerful APIs—Weather API and Google Maps API—to provide users with a range of functionalities, including:

Weather Information: Search and retrieve current weather details by entering city names.
Directions: Get driving directions between two cities.
Data Management: Store, read, edit, and delete data within the application.
Features
Weather Search:

Users can input a city name to retrieve real-time weather data.
The weather information includes temperature, humidity, wind speed, and more.
Directions:

Users can enter the names of two cities to get driving directions.
The application provides step-by-step directions along with the estimated travel time and distance.
Data Management:

The application allows users to store weather searches, favorite locations, or routes.
Users can also read, edit, or delete the stored data.
Installation
Prerequisites
Java 8 or higher
Maven (for managing dependencies)
API keys for both the Weather API and Google Maps API
API Keys
To use this application, you need to obtain API keys for both the Weather API and Google Maps API. Follow these steps:

Weather API:
Visit the official Weather API provider's website (e.g., OpenWeatherMap) and sign up for an API key.
Google Maps API:
Visit the Google Cloud Console.
Create a new project and enable the "Maps JavaScript API" and "Directions API".
Obtain an API key.
Setup
Clone the repository:

bash
Copy code
git clone https://github.com/yourusername/weather-maps-app.git
cd weather-maps-app
Configure API keys:

Create a configuration file (e.g., config.properties) in the src/main/resources directory.
Add your API keys to this file:
properties
Copy code
weather.api.key=YOUR_WEATHER_API_KEY
google.maps.api.key=YOUR_GOOGLE_MAPS_API_KEY
Build the application:

bash
Copy code
mvn clean install
Run the application:

bash
Copy code
java -jar target/weather-maps-app-1.0.jar
Usage
Weather Search
Enter the name of the city in the designated input field.
Click the "Search Weather" button.
The application will display the current weather details.
Get Directions
Enter the names of the starting city and the destination city.
Click the "Get Directions" button.
The application will display the directions, estimated travel time, and distance.
Data Management
Store Data:

Use the "Save" button to store weather information or routes.
Read Data:

Access stored data by navigating to the "Saved Data" section.
Edit Data:

Select an entry from the stored data and make changes, then save it.
Delete Data:

Choose an entry from the stored data and click "Delete" to remove it

Dependencies
Google Maps API: For getting directions between cities.
Weather API: For fetching current weather data.
Gson: For parsing JSON responses.
Maven: For dependency management.
