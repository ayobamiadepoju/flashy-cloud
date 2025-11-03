package hng.backend.fashion_agent.tools;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OutfitService {

    private static final Gson gson = new Gson();

    @Tool(description = "Suggest an outfit based on current weather data.")
    public static Map<String, Object> getSuggestedOutfit(
            @ToolParam(description = "Current weather data as JSON string")
            String weatherJson) {

        Map<String, Object> response = new HashMap<>();

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> weatherData = tryParseJson(weatherJson);

            if (weatherData == null || weatherData.isEmpty()) {
                response.put("error", "No weather data");
                response.put("outfit_suggestion", "Unable to suggest outfit without weather info.");
                return response;
            }

            Object tempObj = weatherData.get("temperature");
            if (!(tempObj instanceof Number)) {
                response.put("error", "Temperature data missing or invalid.");
                response.put("outfit_suggestion", "Unable to provide a recommendation without temperature info.");
                return response;
            }

            double temperature = ((Number) tempObj).doubleValue();
            String condition = weatherData.getOrDefault("condition", "unknown").toString();
            String summary = weatherData.getOrDefault("summary", "No summary available").toString();

            String suggestion;

            // 🌤️ Outfit logic
            if (temperature > 30) {
                suggestion = "Wear light, airy outfits such as cotton shirts, sleeveless tops, and shorts. Sunglasses recommended! 😎";
            } else if (temperature > 25) {
                suggestion = "A casual shirt or t-shirt with chinos or jeans fits well. Consider sandals or light sneakers.";
            } else if (temperature > 18) {
                suggestion = "Go for a checkered shirt with denim or chinos, maybe add a light jacket.";
            } else if (temperature > 10) {
                suggestion = "Wear a sweater or hoodie over your shirt, with trousers or jeans.";
            } else {
                suggestion = "Bundle up with a coat, thick trousers, and warm accessories like gloves and a scarf.";
            }

            // Add condition-based suggestions
            if (condition.toLowerCase().contains("rain")) {
                suggestion += " Don’t forget a waterproof jacket or umbrella!";
            } else if (condition.toLowerCase().contains("cloud")) {
                suggestion += " Layering works best for cloudy days — add a light jacket.";
            }

            response.put("weather_summary", summary);
            response.put("outfit_suggestion", suggestion);
            response.put("error", null);
            return response;

        } catch (Exception e) {
            response.put("error", "Error parsing weather data: " + e.getMessage());
            response.put("outfit_suggestion", "Unable to provide a recommendation.");
            return response;
        }
    }

    // 🔧 Helper to handle both plain and stringified JSON
    private static Map<String, Object> tryParseJson(String json) {
        try {
            return gson.fromJson(json, Map.class);
        } catch (JsonSyntaxException e) {
            try {
                String inner = gson.fromJson(json, String.class);
                return gson.fromJson(inner, Map.class);
            } catch (Exception ignored) {
                return null;
            }
        }
    }

    /**
     * Extracts location from user text.
     * Looks for "in <city>", "at <city>", "for <city>".
     */
    public String extractLocation(String text) {
        Pattern pattern = Pattern.compile("\\b(?:in|at|for)\\s+([A-Za-z]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * Checks if the location exists; if not, asks user for it.
     */
    public Map<String, Object> ensureLocation(String userText) {
        Map<String, Object> response = new HashMap<>();
        String location = extractLocation(userText);

        if (location == null) {
            response.put("error", "Location not found in text");
            response.put("outfit_suggestion", "Please tell me your location so I can suggest an outfit based on the weather.");
        } else {
            response.put("error", null);
            response.put("location", location);
        }

        return response;
    }
}