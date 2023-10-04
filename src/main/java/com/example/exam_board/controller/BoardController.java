package com.example.exam_board.controller;

import com.example.exam_board.dto.ArticleCommentDto;
import com.example.exam_board.dto.ArticleForm;
import com.example.exam_board.entity.Article;
import com.example.exam_board.repository.ArticleRepository;
import com.example.exam_board.service.ArticleService;
import com.example.exam_board.service.PageService;
import com.example.exam_board.service.PaginationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RequiredArgsConstructor
@Controller
@RequestMapping("articles")
public class BoardController {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    PageService pageService;


    public BoardController(ArticleService articleService){
        this.articleService = articleService;
    }

    @Autowired
    ArticleService articleService;


    @GetMapping("/list")
    public String viewList(Model model){
    List<ArticleForm> dtoLists = articleService.viewList();
    model.addAttribute("dtoLists", dtoLists);
    return "view";
    }

    @Autowired
    PaginationService paginationService;
    @GetMapping("/lists")
    public String boardList(Model model,
                            @PageableDefault(page=0, size=10, sort="createdAt",
                            direction = Sort.Direction.DESC)
                            String keyword, String searchType, Pageable pageable) {

        Page<Article> paging = null;
        if (keyword == null) {
             paging = pageService.boardList(pageable);

        } else {
            switch (searchType) {
                case "title":
                    paging = pageService.search(keyword, pageable);
                    break;
                case "content":
                    paging = pageService.searchContent(keyword, pageable);
                    break;
                case "id":
                    paging = pageService.searchId(keyword, pageable);
                    break;
                case "nickname":
                    paging = pageService.searchNickname(keyword, pageable);
                    break;
            }
        }
        int totalPage = paging.getTotalPages();
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(
                pageable.getPageNumber(),
                totalPage);
        model.addAttribute("paging", paging);
        model.addAttribute("paginationBarNumbers", barNumbers);
        return "view";
    }




//    @GetMapping("/article/{id}")
//    public String view(@PathVariable Long id){
//        return "detail";
//    }

//    @GetMapping("/article/{id}")
//    public String view(@PathVariable Long id, Model model) {
//
//        model.addAttribute("article", articleService.findById(id));
//        return "detail";
//    }

    @GetMapping("/lists/new")
    @PreAuthorize("isAuthenticated()")
    public String insert(Model model){
        model.addAttribute("dto", new ArticleForm());
        return "articles/new";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String newArticleSave(ArticleForm dto, Principal principal){
        Long id;
        articleService.articleSave(dto, principal);
        return "redirect:/articles/lists";
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String show(@PathVariable Long id, Model model){
        Article article = articleService.getOneArticle(id);

        //댓글 담을 DTO 가져가기
        model.addAttribute("articleDto", new ArticleCommentDto());
        model.addAttribute("dto", article);
        return "articles/detail";
    }

    @GetMapping("/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String articleDelete(@PathVariable Long id){
        articleRepository.deleteById(id);
        return "redirect:/articles/lists";
    }

    @GetMapping("/{id}/update")
    @PreAuthorize("isAuthenticated()")
    public String update(@PathVariable Long id, Model model){
        model.addAttribute("article", articleService.findById(id));
        return "articles/update";
    }

    @PostMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public String articleUpdate(ArticleForm dto){
        articleService.update(dto);

        return "redirect:/articles/lists";
    }

    @PostMapping("/{id}/articleComment")
    public String commentSave(@PathVariable Long id, ArticleCommentDto dto,
                              Principal principal){
        articleService.commentSave(id, dto, principal);
        return "redirect:/articles/{id}";
    }

    @PostMapping("/{id}/articleComments/{article-comment-id}/delete")
    public String commentDelete(@PathVariable Long id, @PathVariable(name = "article-comment-id") Long commentId){
        articleService.commentDelete(id, commentId);
        return "redirect:/articles/{id}";
    }


}

