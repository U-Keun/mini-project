package com.example.miniproject.controller;


import com.example.miniproject.dto.*;
import com.example.miniproject.entity.SolvedQuiz;
import com.example.miniproject.entity.User;
import com.example.miniproject.security.UserDetailsImpl;
import com.example.miniproject.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    // 퀴즈 게시글 등록
    @PostMapping("/register")
    public BasicResponseDto register(@RequestBody QuizRequestDto quizRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return quizService.register(quizRequestDto, userDetails.getUser());
    }


    // 전체 퀴즈 리스트
    @GetMapping
    public List<QuizResponseDto> findAll() {
        return quizService.findAll();
    }

    // 퀴즈 게시글 조회
    @GetMapping("/{quiz_id}")
    public SolvingQuizResponseDto findById(@PathVariable Long quiz_id) {
        return quizService.findById(quiz_id);
    }

    // 해결한 퀴즈 조회
    @GetMapping("/solved/{user_id}")
    public List<SolvedQuiz> solvedQuizByUser(@PathVariable Long user_id) {
        List<SolvedQuiz> solvedQuizList = quizService.SolvedListByUser(user_id);
        return solvedQuizList;
    }

    // 문제 해결
    @PostMapping("/solved/{quiz_id}")
    public String quizSolvedComplete(@PathVariable Long quiz_id, @RequestBody AnswerRequestDto answerRequestDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return quizService.solvingQuiz(quiz_id, answerRequestDto, user);
    }

    // 퀴즈 게시글 수정하기
    @PutMapping("/{quiz_id}")
    public QuizResponseDto update(@PathVariable Long quiz_id, @RequestBody AmendRequestDto amendRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return quizService.update(quiz_id, amendRequestDto, userDetails.getUser());
    }


    // 퀴즈 게시글 삭제하기
    @DeleteMapping("/{quiz_id}")
    public MsgResponseDto deleteAll(@PathVariable Long quiz_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return quizService.deleteAll(quiz_id, userDetails.getUser());
    }



}
