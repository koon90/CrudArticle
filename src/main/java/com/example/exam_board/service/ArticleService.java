package com.example.exam_board.service;

import com.example.exam_board.dto.ArticleCommentDto;
import com.example.exam_board.dto.ArticleForm;
import com.example.exam_board.entity.Article;
import com.example.exam_board.entity.ArticleComment;
import com.example.exam_board.entity.UserAccount;
import com.example.exam_board.repository.ArticleCommentRepository;
import com.example.exam_board.repository.ArticleRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@RequiredArgsConstructor
@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    ArticleCommentRepository articleCommentRepository;

    @Autowired
    EntityManager em;

    public List<ArticleForm> viewList() {
        List<Article> articles = articleRepository.findAll();
        List<ArticleForm> dtoLists = new ArrayList<>();
        for(Article article : articles){
            ArticleForm articleForm = ArticleForm.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .build();
            dtoLists.add(articleForm);
        }
        dtoLists.forEach(article -> System.out.println(article));
        return dtoLists;
    }

    public ArticleService(ArticleRepository articleRepository){

        this.articleRepository = articleRepository;
    }

    public Article findById(Long id){
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    public Page<Article> boardList(Pageable pageable) {
        //기존 List<Board>값으로 넘어가지만 페이징 설정을 해주면 Page<Board>로 넘어갑니다.
        return articleRepository.findAll(pageable);
    }

    @Transactional
    public void articleSave(ArticleForm articleForm, Principal principal){
        UserAccount account = em.find(UserAccount.class, principal.getName());

        Article article = Article.builder()
                .title(articleForm.getTitle())
                .content(articleForm.getContent())
                .userAccount(account)
                .build();
        account.getArticles().add(article);
        em.persist(article);
    }

    public Article getOneArticle(Long id){
        Article article = em.find(Article.class, id);
        return article;
    }

    @Transactional
    public void update(ArticleForm dto){
        Article article = articleRepository.findById(dto.getId()).get();
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
    }

    @Transactional
    public void commentSave(Long id, ArticleCommentDto dto, Principal principal) {
        Article article = articleRepository.findById(id).get();
        ArticleComment comment = ArticleComment.builder()
                .article(article)
                .content(dto.getContent())
                .userAccount(article.getUserAccount()).build();
        article.getArticleCommentList().add(comment);
    }

    @Transactional
    public void commentDelete(Long id, Long commentId){
        Article article = articleRepository.findById(id).get();
        ArticleComment articleComment = articleCommentRepository.findById(commentId).get();
        article.getArticleCommentList().remove(articleComment);
    }


}