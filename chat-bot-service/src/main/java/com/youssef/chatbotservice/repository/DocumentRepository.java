package com.youssef.chatbotservice.repository;

import com.youssef.chatbotservice.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query(value = "SELECT *, embedding <-> CAST(:vector AS vector) AS distance " +
            "FROM documents ORDER BY distance LIMIT :limit", nativeQuery = true)
    List<Document> findSimilar(@Param("vector") float[] vector, @Param("limit") int limit);


    Optional<Document> findByFilename(String filename);

    // Vérifier si un fichier existe
    boolean existsByFilename(String filename);

    // Récupérer les documents traités
    List<Document> findByProcessed(Boolean processed);

    // Récupérer les documents non traités
    @Query("SELECT d FROM Document d WHERE d.processed = false ORDER BY d.uploadDate ASC")
    List<Document> findUnprocessedDocuments();

    // Compter les documents traités
    long countByProcessed(Boolean processed);

    // Récupérer les documents récents
    @Query("SELECT d FROM Document d ORDER BY d.uploadDate DESC")
    List<Document> findRecentDocuments();
}
