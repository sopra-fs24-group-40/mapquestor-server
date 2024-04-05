package ch.uzh.ifi.hase.soprafs24.config;

import ch.uzh.ifi.hase.soprafs24.controller.GameWebSocketController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
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