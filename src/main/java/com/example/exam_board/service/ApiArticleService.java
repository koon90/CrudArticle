package com.example.exam_board.service;

import com.example.exam_board.dto.ArticleForm;
import com.example.exam_board.entity.Article;
import com.example.exam_board.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApiArticleService {
    @Autowired
    ArticleRepository articleRepository;
    public List<ArticleForm> viewList() {
        List<ArticleForm> lists = new ArrayList<>();
        List<Article> articles = articleRepository.findAll();
        for(Article article : articles){
            ArticleForm form = new ArticleForm();
            form.setId(article.getId());
            form.setTitle(article.getTitle());
            form.setContent(article.getContent());
            lists.add(form);
        }
        return lists;
    }

    public ResponseEntity<ArticleForm> getOneList(@PathVariable Long id) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        ArticleForm form = new ArticleForm();
        form.setId(article.getId());
        form.setTitle(article.getTitle());
        form.setContent(article.getContent());
        return ResponseEntity.status(HttpStatus.OK).body(form);
    }

    @Transactional
    public Article insertList(ArticleForm form) {
        Article article = new Article();
        article.setId(form.getId());
        article.setTitle(form.getTitle());
        article.setContent(form.getContent());
        return articleRepository.save(article);
    }

    @Transactional
    public ResponseEntity<ArticleForm> deleteList(Long id) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            ArticleForm form = new ArticleForm();
            form.setId(article.getId());
            form.setTitle(article.getTitle());
            form.setContent(article.getContent());
            articleRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    @Transactional
    public ResponseEntity<ArticleForm> patchList(Long id, ArticleForm form) {
        Article article = articleRepository.findById(id).orElse(null);
        if (id != form.getId() || article == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            article.setTitle(form.getTitle());
            article.setContent(form.getContent());
            Article updatedArticle = articleRepository.save(article);
            ArticleForm form1 = ArticleForm.builder()
                    .id(updatedArticle.getId())
                    .title(updatedArticle.getTitle())
                    .content(updatedArticle.getContent())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(form1);
        }
    }
}
