package com.svf.mecatool.business.services.implementations;

import com.svf.mecatool.business.services.NotificationService;
import com.svf.mecatool.integration.model.Notification;
import com.svf.mecatool.integration.repositories.NotificationRepository;
import com.svf.mecatool.presentation.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setUserId(notificationDTO.getUserId());
        notification.setTitle(notificationDTO.getTitle());
        notification.setMessage(notificationDTO.getMessage());
        notification.setType(notificationDTO.getType());
        notification.setIsRead(false);

        Notification savedNotification = notificationRepository.save(notification);
        return convertToDTO(savedNotification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getUserNotifications(Integer userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getUnreadUserNotifications(Integer userId) {
        return notificationRepository.findByUserIdAndIsReadOrderByCreatedAtDesc(userId, false)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NotificationDTO markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setIsRead(true);
        return convertToDTO(notificationRepository.save(notification));
    }

    @Override
    @Transactional
    public void markAllAsRead(Integer userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndIsReadOrderByCreatedAtDesc(userId, false);
        unreadNotifications.forEach(notification -> notification.setIsRead(true));
        notificationRepository.saveAll(unreadNotifications);
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    @Override
    @Transactional
    public void sendDeliveryReminder(Integer userId, String deliveryDetails) {
        NotificationDTO notification = new NotificationDTO();
        notification.setUserId(userId);
        notification.setTitle("Delivery Reminder");
        notification.setMessage(deliveryDetails);
        notification.setType("delivery");
        createNotification(notification);
    }

    @Override
    @Transactional
    public void sendMaintenanceReminder(Integer userId, String maintenanceDetails) {
        NotificationDTO notification = new NotificationDTO();
        notification.setUserId(userId);
        notification.setTitle("Maintenance Reminder");
        notification.setMessage(maintenanceDetails);
        notification.setType("maintenance");
        createNotification(notification);
    }

    @Override
    @Transactional
    public void sendPaymentReminder(Integer userId, String paymentDetails) {
        NotificationDTO notification = new NotificationDTO();
        notification.setUserId(userId);
        notification.setTitle("Payment Reminder");
        notification.setMessage(paymentDetails);
        notification.setType("payment");
        createNotification(notification);
    }

    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUserId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setIsRead(notification.getIsRead());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setUpdatedAt(notification.getUpdatedAt());
        return dto;
    }
} 