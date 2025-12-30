package com.fd.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 测试题目表(ExamQuestion)表实体类
 *
 * @author makejava
 * @since 2025-12-29 18:56:45
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("exam_question")
public class ExamQuestion{
    //题目ID
    @TableId
    private Long questionId;
    //关联试卷ID
    @TableField
    private Long examId;
    //关联单词ID
    private Long wordId;
    //题型：1-看中文选英文 2-看英文选中文
    private Integer questionType;
    //题干（中文/英文）
    private String questionStem;
    //正确答案的位置（0/1/2/3）
    private Integer correctAnswer;

    private String option0;
    private String option1;
    private String option2;
    private String option3;
    //用户作答（0/1/2/3）
    private Integer userAnswer;
    //是否答对：1-是 0-否
    private Integer isCorrect;




}

