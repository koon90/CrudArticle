package com.example.exam_board.api;

import com.example.exam_board.dto.ArticleForm;
import com.example.exam_board.entity.Article;
import com.example.exam_board.service.ApiArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArticleApiController {

    @Autowired
    ApiArticleService apiArticleService;

    @GetMapping("/api/articles")
    public List<ArticleForm> getLists(){
        return apiArticleService.viewList();
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleForm> getOneList(@PathVariable Long id){
        return apiArticleService.getOneList(id);
    }

    @PostMapping("/api/article")
    public Article insert(@RequestBody ArticleForm form){
        return apiArticleService.insertList(form);
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<ArticleForm> delete(@PathVariable Long id){
        return apiArticleService.deleteList(id);
    }

    @PatchMapping("/api/articles/{id}")
    public ResponseEntity<ArticleForm> patch(@PathVariable Long id,
                                         @RequestBody ArticleForm form){
        return apiArticleService.patchList(id, form);
    }
}
