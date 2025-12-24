package com.fd.service;
 import com.baomidou.mybatisplus.extension.service.IService;
 import com.fd.domain.ResponseResult;
 import com.fd.domain.entity.Word;

/**
 * 单词表(Word)表服务接口
 *
 * @author makejava
 * @since 2025-12-22 11:23:10
 */
 public interface WordService extends IService<Word> {

 ResponseResult listWords(Integer pageNum, Integer pageSize, Long id,Integer status);

 ResponseResult getWordDetails(Long id);
}
