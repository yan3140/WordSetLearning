package com.fd.controller;

import com.fd.domain.ResponseResult;
import com.fd.service.ExamQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    ExamQuestionService examQuestionService;

    @GetMapping("/{examId}")
    public ResponseResult getQuestions(@PathVariable Long examId) {
        return examQuestionService.getQuestions(examId);
    }
}
