package hng.backend.fashion_agent.controller;

import com.google.gson.Gson;
import hng.backend.fashion_agent.model.MCPResponse;
import hng.backend.fashion_agent.tools.OutfitService;
import hng.backend.fashion_agent.tools.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/tools")
public class MCPController {

    private final Gson gson = new Gson();

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private OutfitService outfitService;

    @PostMapping("/invoke")
    public ResponseEntity<MCPResponse> invokeTool(@RequestBody String requestText) {

        requestText = requestText.trim().toLowerCase();
        MCPResponse response = new MCPResponse();
        response.setRequest(requestText);

        try {
            if (requestText.contains("outfit") || requestText.contains("clothes")) {
                String location = outfitService.extractLocation(requestText);

                if (location == null) {
                    response.setResponse("Please tell me your location so I can suggest an outfit based on your weather.");
                } else {
                    // Get weather data as a Map
                    Map<String, Object> weatherMap = weatherService.getCurrentWeather(location);

                    // Convert it to JSON string for OutfitService
                    String weatherJson = gson.toJson(weatherMap);

                    // Pass JSON string to OutfitService
                    Map<String, Object> outfit = outfitService.getSuggestedOutfit(weatherJson);

                    response.setResponse(outfit);
                }
            } else {
                response.setResponse("I'm not designed to handle that kind of request. Please ask me about weather or outfit suggestions.");
            }

        } catch (Exception e) {
            response.setResponse("Oops! Something went wrong: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}