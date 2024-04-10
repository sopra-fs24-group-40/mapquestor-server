package ch.uzh.ifi.hase.soprafs24.additional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.web.socket.TextMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public abstract class GameMessage {
    public static ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    public static Map<String, Class<?>> registry = new ConcurrentHashMap<>();

    private String type;

    public GameMessage(String type) {
        this.type = type;
        registry.put(type, this.getClass());
    }

    @SneakyThrows
    public static <T extends GameMessage> T fromTextMessage(TextMessage message) {
        JsonNode jsonNode = mapper.readTree(message.getPayload());
        Class<?> type = registry.get(jsonNode.get("type").asText());

        return (T) mapper.treeToValue(jsonNode, type);
    }


    @SneakyThrows
    public TextMessage toTextMessage() {
        return new TextMessage(mapper.writeValueAsString(this));
    }

}
