package com.svf.mecatool.integration.repositories;

import com.svf.mecatool.integration.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdAndIsReadOrderByCreatedAtDesc(Integer userId, Boolean isRead);
    List<Notification> findByUserIdOrderByCreatedAtDesc(Integer userId);
} 