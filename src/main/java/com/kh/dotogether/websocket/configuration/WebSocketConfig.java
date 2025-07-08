package com.kh.dotogether.websocket.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.kh.dotogether.websocket.configuration.handler.ChatWebSocketHandler;
import com.kh.dotogether.websocket.configuration.handler.WorkWebSocketHandler;
import com.kh.dotogether.websocket.configuration.handshake.WebSocketAuthInterceptor;

import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer{
   
   private final ChatWebSocketHandler chatHandler;
   private final WorkWebSocketHandler workHandler;
   private final WebSocketAuthInterceptor webSocketAuthInterceptor;

   @Override
   public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
      
      registry.addHandler(chatHandler, "/ws/chat/{roomId}")
            .addInterceptors(webSocketAuthInterceptor)
            .setAllowedOrigins("http://54.180.120.189");
      
      registry.addHandler(workHandler, "/ws/work/{roomId}")
            .addInterceptors(webSocketAuthInterceptor)
            .setAllowedOrigins("http://54.180.120.189");
   }

}
