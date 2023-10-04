package com.example.exam_board.repository;

import com.example.exam_board.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findByTitleContaining(String keyword, Pageable pageable);
    Page<Article> findByContentContaining(String keyword, Pageable pageable);
    Page<Article> findById(Long keyword, Pageable pageable);
    Page<Article> findByCreatedByContaining(String keyword, Pageable pageable);
}
