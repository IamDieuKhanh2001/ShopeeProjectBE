package com.example.fsoft_shopee_nhom02.Notification;

import com.example.fsoft_shopee_nhom02.config.GlobalVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static com.example.fsoft_shopee_nhom02.config.GlobalVariable.*;

@Service
public class NotificationService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendNotification(String message, String UserId) {
        simpMessagingTemplate.convertAndSendToUser(UserId, Notification_destination, message);
    }
}
