package com.svf.mecatool.business.services;

import com.svf.mecatool.presentation.dto.NotificationDTO;
import java.util.List;

public interface NotificationService {
    NotificationDTO createNotification(NotificationDTO notificationDTO);
    List<NotificationDTO> getUserNotifications(Integer userId);
    List<NotificationDTO> getUnreadUserNotifications(Integer userId);
    NotificationDTO markAsRead(Long notificationId);
    void markAllAsRead(Integer userId);
    void deleteNotification(Long notificationId);
    void sendDeliveryReminder(Integer userId, String deliveryDetails);
    void sendMaintenanceReminder(Integer userId, String maintenanceDetails);
    void sendPaymentReminder(Integer userId, String paymentDetails);
} 