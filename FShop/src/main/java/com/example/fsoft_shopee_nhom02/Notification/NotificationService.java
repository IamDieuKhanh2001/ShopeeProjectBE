package com.example.fsoft_shopee_nhom02.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static com.example.fsoft_shopee_nhom02.Notification.NotificationVar.*;

@Service
public class NotificationService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendNotification(String message, String UserId) {
        simpMessagingTemplate.convertAndSendToUser(UserId, destination, message);
    }
}
