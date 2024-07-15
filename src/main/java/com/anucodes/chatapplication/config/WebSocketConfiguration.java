package com.anucodes.chatapplication.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final List<WebSocketSession> sessionList = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler() {
                    @Override
                    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                        sessionList.add(session);
                    }

                    @Override
                    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

                    }

                    @Override
                    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

                    }

                    @Override
                    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                        sessionList.remove(session);
                    }

                    @Override
                    public boolean supportsPartialMessages() {
                        return false;
                    }
                }, "/api/socket");
    }
}
