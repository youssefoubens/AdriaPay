package com.youssef.chatbotservice.services;

import org.springframework.stereotype.Service;

@Service
public interface ChatBotService {
    String askQuestion(String query);
}

