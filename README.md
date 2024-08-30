# Weather and Maps Application
## Overview
This Java application integrates two powerful APIs—Weather API and Google Maps API—to provide users with a range of functionalities, including:
•	Weather Information: Search and retrieve current weather details by entering city names.
•	Directions: Get driving directions between two cities.
•	Data Management: Store, read, edit, and delete data within the application.
## Features
1.	Weather Search:
o	Users can input a city name to retrieve real-time weather data.
o	The weather information includes temperature, humidity, wind speed, and more.
2.	Directions:
o	Users can enter the names of two cities to get driving directions.
o	The application provides step-by-step directions along with the estimated travel time and distance.
3.	Data Management:
o	The application allows users to store weather searches, favorite locations, or routes.
o	Users can also read, edit, or delete the stored data.
## Installation
### Prerequisites
•	Java 8 or higher
•	Maven (for managing dependencies)
•	API keys for both the Weather API and Google Maps API
## API Keys
To use this application, you need to obtain API keys for both the Weather API and Google Maps API. Follow these steps:
1.	Weather API:
o	Visit the official Weather API provider's website (e.g., OpenWeatherMap) and sign up for an API key.
2.	Google Maps API:
o	Visit the Google Cloud Console.
o	Create a new project and enable the "Maps JavaScript API" and "Directions API".
o	Obtain an API key.
## Setup
1.	Clone the repository:
bash
### Copy code
git clone https://github.com/Hussnain21/WeatherRoute.git 
cd weather-maps-app
2.	Configure API keys:
o	Create a configuration file (e.g., config.properties) in the src/main/resources directory.
o	Add your API keys to this file:
## properties
### Copy code
weather.api.key=YOUR_WEATHER_API_KEY
google.maps.api.key=YOUR_GOOGLE_MAPS_API_KEY
3.	Build the application:
bash
Copy code
mvn clean install
4.	Run the application:
bash
Copy code
java -jar target/weather-maps-app-1.0.jar
## Usage
## Weather Search
1.	Enter the name of the city in the designated input field.
2.	Click the "Search Weather" button.
3.	The application will display the current weather details.
## Get Directions
1.	Enter the names of the starting city and the destination city.
2.	Click the "Get Directions" button.
3.	The application will display the directions using polyine.
## Data Management
1. 	Store Data:
o	Use the "Save" button to store weather information or routes.
2.	Read Data:
o	Access stored data by navigating to the "Saved Data" section.
3.	Edit Data:
o	Select an entry from the stored data and make changes, then save it.
4.	Delete Data:
o	Choose an entry from the stored data and click "Delete" to remove it.


## Dependencies
•	Google Maps API: For getting directions between cities.
•	Weather API: For fetching current weather data.
•	Gson: For parsing JSON responses.
•	Maven: For dependency management.

