package com.fd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fd.domain.ResponseResult;
import com.fd.domain.entity.UserExam;

/**
 * 用户测试表(UserExam)表服务接口
 *
 * @author makejava
 * @since 2025-12-29 18:57:13
 */
public interface UserExamService extends IService<UserExam> {
    ResponseResult getExams(Integer status);

 ResponseResult createExam(Long bookId, Integer questionCount, Double questionRatio, Integer examTimeLimit);
}
