package com.example.miniproject.service;

import com.example.miniproject.dto.*;
import com.example.miniproject.entity.Quiz;
import com.example.miniproject.entity.SolvedQuiz;
import com.example.miniproject.entity.User;
import com.example.miniproject.repository.QuizRepository;
import com.example.miniproject.repository.SolvedQuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final SolvedQuizRepository solvedQuizRepository;


    // 퀴즈 등록
    public BasicResponseDto<?> register(QuizRequestDto quizRequestDto, User user) {
        Quiz quiz = new Quiz(quizRequestDto, user.getUserId());
        quizRepository.save(quiz);
        return BasicResponseDto.setSuccess("퀴즈 등록 성공!", null);
    }



    // 개별 퀴즈 조회
    public BasicResponseDto<SolvingQuizResponseDto> findById(Long id) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 퀴즈가 없습니다."));
        List<String> answerList = new ArrayList<>();

        answerList.add(quiz.getCorrect());
        if (quiz.getIncorrect1()!=null) {answerList.add(quiz.getIncorrect1());}
        if (quiz.getIncorrect2()!=null) {answerList.add(quiz.getIncorrect2());}
        if (quiz.getIncorrect3()!=null) {answerList.add(quiz.getIncorrect3());}
        Collections.shuffle(answerList);

        return BasicResponseDto.setSuccess("퀴즈 조회 성공!", new SolvingQuizResponseDto(quiz, answerList));
    }

    // 전체 퀴즈 조회
    public BasicResponseDto<List<QuizResponseDto>> findAll() {
        List<Quiz> quizzes = quizRepository.findAll();
        return BasicResponseDto.setSuccess("전체 게시글 조회 성공!", quizzes.stream().map(QuizResponseDto::new).collect(Collectors.toList()));
    }

    // 해결한 문제 조회
    @Transactional(readOnly = true)
    public List<SolvedQuiz> SolvedListByUser(Long id) {

        return solvedQuizRepository.selectSolvedQuiz(id);
    }

    // 문제 해결하는 API
    @Transactional
    public String solvingQuiz(long quizId, AnswerRequestDto answerRequestDto, User user) {
        // userId, quizId 받고
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(IllegalArgumentException::new);
        SolvedQuiz solvedQuiz = solvedQuizRepository.findByUserIdAndQuizId(user.getId(), quizId);
        // solvedQuiz.setSolved(true)를 위해서 정답을 json으로 입력받았을 때,
        if (quiz.getCorrect().equals(answerRequestDto)) {
            // 정답과 equals면 setsolved(true)
            if (!solvedQuiz.isSolved()) { // 이미 푼 문제 제외
                solvedQuiz.setSolved(true);
                solvedQuizRepository.save(solvedQuiz);
            }
            solvedQuizRepository.save(solvedQuiz);
            return "redirect:/quiz";
        } else {
            return "redirect:/quiz/"+quizId;
        }
    }

    // 퀴즈 게시물 수정
    @Transactional
    public BasicResponseDto<?> update(Long id, AmendRequestDto amendRequestDto, User user) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 퀴즈가 없습니다.")
        );

        if(!StringUtils.equals(quiz.getId(), user.getId())) {
            return BasicResponseDto.setFailed("퀴즈의 작성자만 수정이 가능합니다.");
        }

        quiz.update(amendRequestDto);
        return BasicResponseDto.setSuccess("퀴즈 수정 성공!", null);
    }

    // 퀴즈 게시물 삭제
    @Transactional
    public BasicResponseDto<?> deleteAll(Long id, User user) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 퀴즈가 없습니다.")
        );

        if(!StringUtils.equals(quiz.getId(), user.getId())) {
            return BasicResponseDto.setFailed("퀴즈의 작성자만 삭제가가 가능합니다.");
        }

        quizRepository.delete(quiz);
        return BasicResponseDto.setSuccess("퀴즈 삭제 성공", null);
    }
}
