package com.svf.mecatool.business.services;

import com.svf.mecatool.integration.model.Notification;
import com.svf.mecatool.integration.model.Notification.NotificationType;

import java.util.List;

public interface NotificationService {
    Notification createNotification(NotificationType type, String message, Integer userId, Long relatedEntityId, String relatedEntityType);
    List<Notification> getAllNotifications();
    List<Notification> getUnreadNotifications();
    Notification markAsRead(Long notificationId);
    void deleteNotification(Long notificationId);
} 