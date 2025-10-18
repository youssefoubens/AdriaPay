package com.youssef.chatbotservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String filename;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "upload_date")
    @CreationTimestamp
    private LocalDateTime uploadDate;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(nullable = false)
    @Builder.Default
    private Boolean processed = false;

    @Column(columnDefinition = "TEXT")
    private String content;


//    @Column(name = "metadata", columnDefinition = "jsonb")
//    private String metadata;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
