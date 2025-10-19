//package com.youssef.chatbotservice.configuration;
//
//import org.springframework.ai.embedding.EmbeddingModel;
//
//import org.springframework.ai.vectorstore.VectorStore;
//import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//@Configuration
//public class VectorStoreConfig {
//
//    @Bean
//    public VectorStore vectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
//        return new PgVectorStore(jdbcTemplate, embeddingModel);
//    }
//}