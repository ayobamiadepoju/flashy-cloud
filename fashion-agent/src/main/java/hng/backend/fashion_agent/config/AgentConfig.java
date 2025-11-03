package hng.backend.fashion_agent.config;

import com.google.adk.agents.BaseAgent;
import hng.backend.fashion_agent.tools.AgentTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgentConfig {

    private static final Logger log = LoggerFactory.getLogger(AgentConfig.class);

    /**
     * Creates and registers the root fashion agent.
     */
    @Bean
    public BaseAgent fashionAgent() {
        log.info("🧠 Initializing Fashion Agent hierarchy...");
        BaseAgent agent = AgentTools.createRootAgent();
        log.info("✅ Fashion Agent initialized successfully.");
        return agent;
    }
}
