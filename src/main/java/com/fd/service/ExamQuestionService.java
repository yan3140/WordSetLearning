package com.fd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fd.domain.ResponseResult;
import com.fd.domain.entity.ExamQuestion;

/**
 * 测试题目表(ExamQuestion)表服务接口
 *
 * @author makejava
 * @since 2025-12-29 18:56:45
 */
public interface ExamQuestionService extends IService<ExamQuestion> {
 ResponseResult getQuestions(Long examId);
}
