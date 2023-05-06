package com.example.miniproject.dto;

import com.example.miniproject.entity.Quiz;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuizResponseDto {
    private Long id;
    private String title;
    private String content;
    private String userId;
    private boolean solved;

    public QuizResponseDto(Quiz quiz) {
        this.id = quiz.getId();
        this.userId = quiz.getUserId();
        this.title = quiz.getTitle();
        this.content = quiz.getContent();
        this.solved = true;
    }
}
