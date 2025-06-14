package com.svf.mecatool.presentation.controller;

import com.svf.mecatool.business.services.NotificationService;
import com.svf.mecatool.integration.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<List<Notification>> getUnreadNotifications() {
        List<Notification> unreadNotifications = notificationService.getUnreadNotifications();
        return ResponseEntity.ok(unreadNotifications);
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id) {
        try {
            Notification updatedNotification = notificationService.markAsRead(id);
            return ResponseEntity.ok(updatedNotification);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        try {
            notificationService.deleteNotification(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 