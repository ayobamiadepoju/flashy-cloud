package hng.backend.fashion_agent.tools;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.FunctionTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class AgentTools {

    private static final Logger log = LoggerFactory.getLogger(AgentTools.class);

    public static BaseAgent createRootAgent() {

        // 🌦️ Weather Agent
        LlmAgent weatherAgent = LlmAgent.builder()
                .name("weather_agent")
                .model("gemini-2.0-flash")
                .description("Retrieves live weather data from OpenWeatherMap for the given location.")
                .instruction("""
                    You are a weather assistant.

                    Behavior:
                      - If the user's message does not include a location, ask: "Please tell me your location."
                      - If a location is provided, extract the city name and call the tool getCurrentWeather(location).
                      - Use the returned weather summary directly as your reply.

                    Example:
                      User: "What's the weather in Lagos?"
                      → (Call getCurrentWeather("Lagos"))
                      → "The weather in Lagos is sunny with 32°C."
                """)
                .tools(FunctionTool.create(WeatherService.class, "getCurrentWeather"))
                .outputKey("weather")
                .build();

        // Fashion Agent – uses weather info to suggest outfits
        return LlmAgent.builder()
                .name("fashion_agent")
                .model("gemini-2.0-flash")
                .description("Suggests outfits based on live weather data.")
                .instruction("""
                    You are a fashion stylist that uses live weather data.

                    Steps:
                        1. Call weather_agent with the user's message to get current weather.
                        2. Suggest an outfit based on the returned weather summary.
                        3. Format your reply as:
                           - Weather summary
                              - Outfit suggestion

                    Examples:
                        - "The weather in Lagos is sunny with 32°C. Go for light linen, open-toe shoes, and sunglasses 😎."
                        - "The weather in London is rainy with 11°C. Try a trench coat, waterproof boots, and a cozy scarf ☔."
                """)
                .tools(AgentTool.create(weatherAgent))
                .tools(FunctionTool.create(OutfitService.class, "getSuggestedOutfit"))
                .outputKey("recommendation")
                .build();
    }
}
