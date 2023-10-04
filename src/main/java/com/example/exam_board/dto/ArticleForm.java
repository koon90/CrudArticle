package com.example.exam_board.dto;

import com.example.exam_board.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleForm {
    private Long id;
    private String title;
    private String content;

}

