package com.fd.controller;

import com.fd.domain.ResponseResult;
import com.fd.domain.dto.ExamDto;
import com.fd.domain.entity.ExamQuestion;
import com.fd.service.UserExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exams")
public class ExamController {

    @Autowired
    private UserExamService userExamService;

    @GetMapping("/{status}")
    public ResponseResult getExams(@PathVariable("status") Integer status) {
        return userExamService.getExams(status);
    }

    @PostMapping
    public ResponseResult createExam(@RequestParam Long bookId,
                                      @RequestParam Integer questionCount,
                                      @RequestParam Double questionRatio,
                                      @RequestParam Integer examTimeLimit) {
        return userExamService.createExam(bookId,questionCount,questionRatio,examTimeLimit);
    }

    @PutMapping
    public ResponseResult updateExamStatus(@RequestBody ExamDto examDto){
        return userExamService.updateExamStatus(examDto);
    }
}
