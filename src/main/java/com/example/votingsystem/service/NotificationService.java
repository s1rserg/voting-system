package com.example.votingsystem.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void notifyUser(Long userId, String message) {
        System.out.println("Notifying user " + userId + ": " + message);
    }
}

