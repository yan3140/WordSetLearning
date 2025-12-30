package com.fd.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionVo {
    //题干（中文/英文）
    private String questionStem;
    //正确答案的位置（0/1/2/3）
    private Integer correctAnswer;

    private String option0;
    private String option1;
    private String option2;
    private String option3;
}
