package hng.backend.fashion_agent.config;

import com.google.adk.agents.BaseAgent;
import hng.backend.fashion_agent.tools.AgentTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MCPAgentInitializer {

    private static final Logger log = LoggerFactory.getLogger(MCPAgentInitializer.class);
    private final BaseAgent fashionAgent;

    public MCPAgentInitializer(BaseAgent fashionAgent) {
        this.fashionAgent = fashionAgent;
    }

    @PostConstruct
    public void registerWithMCP() {
        log.info("🔗 Registering Fashion Agent with MCP runtime...");
        try {
            AgentTools.createRootAgent();
            log.info("✅ Fashion Agent registered with MCP successfully.");
        } catch (Exception e) {
            log.error("❌ Failed to register Fashion Agent with MCP", e);
        }
    }
}
