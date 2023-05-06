package com.example.miniproject.dto;


import com.example.miniproject.entity.Comment;
import com.example.miniproject.entity.Quiz;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class SolvingQuizResponseDto {
    private Long id;
    private String title;
    private String content;
    private List<String> answerList;
    private String userId;
    private boolean solved;
    private List<CommentResponseDto> commentList;

    public SolvingQuizResponseDto(Long id, String title, String content, List<String> answerList, String userId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.answerList = answerList;
        this.userId = userId;
    }

    public SolvingQuizResponseDto(Quiz quiz, List<String> answerList) {
        this.id = quiz.getId();
        this.title = quiz.getTitle();
        this.content = quiz.getContent();
        this.answerList = answerList;
        this.userId = quiz.getUserId();
        this.solved = true;
        this.commentList = quiz.getCommentList().stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
}
