package com.youssef.chatbotservice.repository;

import com.youssef.chatbotservice.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySessionIdOrderByCreatedAtAsc(String sessionId);

    // Récupérer l'historique par utilisateur
    List<ChatMessage> findByUserIdOrderByCreatedAtDesc(String userId);

    // Récupérer les messages récents
    @Query("SELECT c FROM ChatMessage c ORDER BY c.createdAt DESC")
    List<ChatMessage> findRecentMessages();

    // Récupérer les messages dans une période
    @Query("SELECT c FROM ChatMessage c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    List<ChatMessage> findMessagesBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // Calculer le temps de réponse moyen
    @Query("SELECT AVG(c.responseTimeMs) FROM ChatMessage c")
    Double calculateAverageResponseTime();

    // Compter les conversations par session
    @Query("SELECT COUNT(DISTINCT c.sessionId) FROM ChatMessage c")
    Long countUniqueSessions();
}
