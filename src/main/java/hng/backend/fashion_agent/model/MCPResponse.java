package hng.backend.fashion_agent.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MCPResponse {

    private String request;
    private Object response;
}