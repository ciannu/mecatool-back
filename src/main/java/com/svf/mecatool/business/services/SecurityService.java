package com.svf.mecatool.business.services;

import com.svf.mecatool.security.details.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("securityService")
public class SecurityService {

    public boolean isCurrentUser(Authentication authentication, Long userId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            CustomUserDetails currentUser = (CustomUserDetails) principal;
            return currentUser.getId().equals(userId);
        }
        return false;
    }
} 