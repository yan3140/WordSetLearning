package com.fd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fd.domain.ResponseResult;
import com.fd.domain.constants.SystemConstants;
import com.fd.domain.entity.Translation;
import com.fd.domain.entity.UserWordStatus;
import com.fd.domain.entity.Word;
import com.fd.domain.vo.TranslationVo;
import com.fd.domain.vo.WordLVo;
import com.fd.mapper.LearnMapper;
import com.fd.mapper.TranslationMapper;
import com.fd.service.LearnService;
import com.fd.service.UserWordStatusService;
import com.fd.utils.BeanCopyUtils;
import com.fd.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LearnServiceImpl extends ServiceImpl<LearnMapper, Word> implements LearnService {

    @Autowired
    private UserWordStatusService userWordStatusService;
    @Autowired
    private TranslationMapper translationMapper;
    @Override
    public ResponseResult reciteWords(Long bookId, Integer count,Integer status) {
        List<WordLVo> words = getWords(bookId, count, status);
        return ResponseResult.okResult(words);
    }

    @Override
    public ResponseResult updateStatus(Long wordId, Integer status) {
        LambdaUpdateWrapper<UserWordStatus> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(UserWordStatus::getUserId,SecurityUtils.getUserId());
        lambdaUpdateWrapper.eq(UserWordStatus::getWordId,wordId);
        lambdaUpdateWrapper.set(UserWordStatus::getStatus,status);
        userWordStatusService.update(lambdaUpdateWrapper);
        return ResponseResult.okResult();
    }

    private List<WordLVo> getWords(Long bookId, Integer count,Integer status) {
        LambdaQueryWrapper<UserWordStatus> userWordStatusQueryWrapper = new LambdaQueryWrapper<>();
        userWordStatusQueryWrapper.eq(UserWordStatus::getUserId, SecurityUtils.getUserId());
        userWordStatusQueryWrapper.in(UserWordStatus::getStatus, status);
        List<Long> wordIds = userWordStatusService.list(userWordStatusQueryWrapper).stream()
                .map(UserWordStatus::getWordId)
                .toList();
        Page<Word> wordPage = new Page<>(1, count);
        LambdaQueryWrapper<Word> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Word::getBookId,bookId);
        queryWrapper.in(Word::getWordId,wordIds);
        List<Word> words = page(wordPage, queryWrapper).getRecords();
        List<WordLVo> wordLVos = BeanCopyUtils.copyBeanList(words, WordLVo.class);
        List<Long> ids = words.stream()
                .map(Word::getWordId)
                .toList();
        LambdaQueryWrapper<Translation> translationQueryWrapper = new LambdaQueryWrapper<>();
        translationQueryWrapper.in(Translation::getWordId,ids);
        Map<Long, TranslationVo> translationVoMap = translationMapper.selectList(translationQueryWrapper).stream()
                .collect(Collectors.groupingBy(
                        Translation::getWordId, // 按wordId分组
                        // 取分组后的第一个元素
                        Collectors.collectingAndThen(
                                // 先转换为TranslationVo列表
                                Collectors.mapping(trans -> BeanCopyUtils.copyBean(trans, TranslationVo.class), Collectors.toList()),
                                // 只保留列表第一个元素（无则为null）
                                list -> list.isEmpty() ? null : list.get(0)
                        )
                ));
        for (WordLVo wordLVo : wordLVos) {
            TranslationVo translationVo = translationVoMap.get(wordLVo.getWordId());
            wordLVo.setTranslation(translationVo);
        }
        return wordLVos;
    }
}
