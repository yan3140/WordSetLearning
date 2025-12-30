package com.fd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fd.domain.ResponseResult;
import com.fd.domain.entity.ExamQuestion;
import com.fd.mapper.ExamQuestionMapper;
import com.fd.service.ExamQuestionService;
import com.fd.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 测试题目表(ExamQuestion)表服务实现类
 *
 * @author makejava
 * @since 2025-12-29 18:56:45
 */
@Service("examQuestionService")
public class ExamQuestionServiceImpl extends ServiceImpl<ExamQuestionMapper, ExamQuestion> implements ExamQuestionService {
    @Override
    public ResponseResult getQuestions(Long examId) {
        LambdaQueryWrapper<ExamQuestion> queryWrapper = new LambdaQueryWrapper<ExamQuestion>();
        queryWrapper.eq(ExamQuestion::getExamId, examId);
        List<ExamQuestion> questions = list(queryWrapper);
     return ResponseResult.okResult(questions);
    }
}

