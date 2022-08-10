package com.example.fsoft_shopee_nhom02.Notification;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public NotificationService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendNotification(String message, Long UserId) {
        simpMessagingTemplate.convertAndSendToUser(UserId.toString(), "/client", message);
    }
}
