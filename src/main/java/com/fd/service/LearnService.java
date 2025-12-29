package com.fd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fd.domain.ResponseResult;
import com.fd.domain.entity.Word;

public interface LearnService extends IService<Word>{
    ResponseResult reciteWords(Long bookId, Integer count,Integer status);

    ResponseResult updateStatus(Long wordId, Integer status);
}
