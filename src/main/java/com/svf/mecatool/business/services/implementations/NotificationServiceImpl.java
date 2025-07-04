package com.svf.mecatool.business.services.implementations;

import com.svf.mecatool.business.services.NotificationService;
import com.svf.mecatool.integration.model.Notification;
import com.svf.mecatool.integration.model.Notification.NotificationType;
import com.svf.mecatool.integration.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public Notification createNotification(NotificationType type, String message, Integer userId, Long relatedEntityId, String relatedEntityType) {
        Notification notification = new Notification();
        notification.setType(type);
        notification.setMessage(message);
        notification.setTitle(type == NotificationType.NEW_WORK_ORDER ? "New Work Order" : "Low Stock Alert");
        notification.setUserId(userId);
        notification.setIsRead(false); // Use setIsRead() as generated by Lombok
        notification.setRelatedEntityId(relatedEntityId);
        notification.setRelatedEntityType(relatedEntityType);
        return notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getAllNotifications() {
        return notificationRepository.findByOrderByCreatedAtDesc(); // Corrected method name
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications() {
        return notificationRepository.findByIsReadFalseOrderByCreatedAtDesc(); // Corrected method name
    }

    @Override
    public Notification markAsRead(Long notificationId) {
        Optional<Notification> notificationOptional = notificationRepository.findById(notificationId);
        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();
            notification.setIsRead(true); // Use setIsRead() as generated by Lombok
            return notificationRepository.save(notification);
        } else {
            throw new RuntimeException("Notification not found with ID: " + notificationId);
        }
    }

    @Override
    public void deleteNotification(Long notificationId) {
        if (notificationRepository.existsById(notificationId)) {
            notificationRepository.deleteById(notificationId);
        } else {
            throw new RuntimeException("Notification not found with ID: " + notificationId);
        }
    }
}