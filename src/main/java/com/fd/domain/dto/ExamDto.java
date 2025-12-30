package com.fd.domain.dto;

import com.fd.domain.entity.ExamQuestion;
import com.fd.domain.entity.UserExam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamDto {
    private UserExam userExam;
    private List<ExamQuestion> examQuestions;
}
