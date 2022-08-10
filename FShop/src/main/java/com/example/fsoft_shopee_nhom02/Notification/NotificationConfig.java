package com.example.fsoft_shopee_nhom02.Notification;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static com.example.fsoft_shopee_nhom02.Notification.NotificationVar.*;

@Configuration
@EnableWebSocketMessageBroker
public class NotificationConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(endpoint).setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // client send
        registry.setApplicationDestinationPrefixes(sendPrefix);
        // server push
        registry.enableSimpleBroker(pushPrefix);
    }
}

