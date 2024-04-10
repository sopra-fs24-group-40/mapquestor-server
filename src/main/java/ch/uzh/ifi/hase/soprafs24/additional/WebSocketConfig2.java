package ch.uzh.ifi.hase.soprafs24.additional;

import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

// @Configuration
// @EnableWebSocket
// @RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WebSocketConfig2 implements WebSocketConfigurer {
   // private final GameWebSocketController gameWebSocketController;


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
   //     registry.addHandler(gameWebSocketController, "/ws").setAllowedOrigins("http://localhost:3000");
    }
}