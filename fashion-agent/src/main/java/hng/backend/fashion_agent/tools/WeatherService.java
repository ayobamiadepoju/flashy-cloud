package hng.backend.fashion_agent.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

    private static final String API_KEY = System.getenv("OPENWEATHER_API_KEY");
    private static final Gson gson = new Gson();

    @Tool(name = "getCurrentWeather", description = "Retrieve current weather of the location")
    public static Map<String, Object> getCurrentWeather(
            @ToolParam(description = "City or region to get weather for")
                                                       String location) {

        if (API_KEY == null || API_KEY.isBlank()) {
            throw new IllegalStateException("Missing API key: set OPENWEATHER_API_KEY in environment variables.");
        }

        try {
            String url = String.format(
                    "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric",
                    location.replace(" ", "%20"),
                    API_KEY
            );

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            if (response == null) {
                return Map.of(
                        "error", "No response",
                        "summary", "Failed to get weather data for " + location
                );
            }

            Map<String, Object> result = parseWeatherResponse(response);

            System.out.println("[WeatherTool] Weather response for " + location + ": " + gson.toJson(result));

            return result;

        } catch (Exception e) {
            return Map.of(
                    "error", e.getMessage(),
                    "summary", String.format("Couldn't fetch weather for %s. Error: %s", location, e.getMessage())
            );
        }
    }

    private static Map<String, Object> parseWeatherResponse(String jsonResponse) {
        try {
            JsonObject data = gson.fromJson(jsonResponse, JsonObject.class);
            JsonObject main = data.getAsJsonObject("main");
            JsonObject weather = data.getAsJsonArray("weather").get(0).getAsJsonObject();

            String condition = weather.get("main").getAsString();
            String description = weather.get("description").getAsString();
            double temp = main.get("temp").getAsDouble();
            int humidity = main.get("humidity").getAsInt();

            String cityName = data.get("name").getAsString();
            String country = data.getAsJsonObject("sys").get("country").getAsString();

            String summary = String.format(
                    "The weather in %s, %s is %s with %.1f°C. %s",
                    cityName, country, description, temp,
                    getWeatherAdvice(temp, condition, humidity)
            );

            Map<String, Object> result = new HashMap<>();
            result.put("location", cityName + ", " + country);
            result.put("temperature", temp);
            result.put("condition", condition);
            result.put("summary", summary);

            return result;

        } catch (Exception e) {
            return Map.of(
                    "error", "Parse error",
                    "summary", e.getMessage()
            );
        }
    }

    private static String getWeatherAdvice(double temp, String condition, int humidity) {
        StringBuilder advice = new StringBuilder();

        if (temp > 30) {
            advice.append("It's very hot - stay hydrated and wear light clothing.");
        } else if (temp > 25) {
            advice.append("It's warm - perfect for light summer wear.");
        } else if (temp > 18) {
            advice.append("Pleasant temperature - dress comfortably.");
        } else if (temp > 10) {
            advice.append("It's cool - consider a light jacket.");
        } else {
            advice.append("It's cold - bundle up with warm layers.");
        }

        if (condition.equalsIgnoreCase("Rain") || condition.toLowerCase().contains("rain")) {
            advice.append(" Don't forget an umbrella or rain jacket!");
        }

        if (humidity > 80) {
            advice.append(" High humidity - choose breathable fabrics.");
        }

        return advice.toString();
    }
}