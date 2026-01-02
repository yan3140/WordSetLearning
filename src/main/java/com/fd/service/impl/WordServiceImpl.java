package com.fd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fd.domain.ResponseResult;
import com.fd.domain.constants.SystemConstants;
import com.fd.domain.entity.*;
import com.fd.domain.vo.*;
import com.fd.enums.AppHttpCodeEnum;
import com.fd.exception.SystemException;
import com.fd.mapper.PhraseMapper;
import com.fd.mapper.SentenceMapper;
import com.fd.mapper.TranslationMapper;
import com.fd.mapper.WordMapper;
import com.fd.service.UserWordStatusService;
import com.fd.service.WordService;
import com.fd.utils.BeanCopyUtils;
import com.fd.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 单词表(Word)表服务实现类
 *
 * @author makejava
 * @since 2025-12-22 11:23:11
 */
@Service("wordService")
public class WordServiceImpl extends ServiceImpl<WordMapper, Word> implements WordService {

    @Autowired
    private WordMapper wordMapper;

    @Autowired
    private UserWordStatusService userWordStatusService;

    @Autowired
    private PhraseMapper phraseMapper;

    @Autowired
    private SentenceMapper sentenceMapper;

    @Autowired
    private TranslationMapper translationMapper;
    @Override
    public ResponseResult listWords(Integer pageNum, Integer pageSize, Long id,Integer status) {
        Long userId = SecurityUtils.getUserId();
        if(Objects.isNull(userId)){
            throw new SystemException(AppHttpCodeEnum.NEED_LOGIN);
        }
        List<Integer> statusList = new ArrayList<>();
        if(SystemConstants.WORD_STATUS_REMEMBERED.equals(status)){
            statusList.add(SystemConstants.WORD_STATUS_REMEMBERED);
        }else{
            statusList.add(SystemConstants.WORD_STATUS_UNREMEMBERED);
            statusList.add(SystemConstants.WORD_STATUS_VAGUE);
        }
        LambdaQueryWrapper<Word> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Word::getBookId, id);
        List<Long> wordIds = list(queryWrapper).stream()
                .map(Word::getWordId)
                .toList();
        LambdaQueryWrapper<UserWordStatus>  queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(UserWordStatus::getUserId, userId)
                .in(UserWordStatus::getWordId, wordIds)
                .in(UserWordStatus::getStatus, statusList);
        Page<UserWordStatus> page = new Page<>(pageNum, pageSize);
        List<UserWordStatus> userWordStatuses = userWordStatusService.page(page, queryWrapper1).getRecords();
        List<Long> wordIdList = userWordStatuses.stream()
                .map(UserWordStatus::getWordId)
                .toList();
        Map<Long, Integer> wordIdFogetCOuntMap = userWordStatuses.stream()
                .collect(Collectors.toMap(UserWordStatus::getWordId, UserWordStatus::getForgetCount));
        //为空情况
        if (CollectionUtils.isEmpty(wordIdList)) {
            WordListVo emptyVo = new WordListVo(List.of(), statusList.get(0), 0L);
            return ResponseResult.okResult(emptyVo);
        }
        LambdaQueryWrapper<Word> wordWrapper = new LambdaQueryWrapper<>();
        wordWrapper.in(Word::getWordId, wordIdList);
        List<Word> wordList = list(wordWrapper);
        List<WordLVo> wordLVos = BeanCopyUtils.copyBeanList(wordList, WordLVo.class);
        LambdaQueryWrapper<Translation> translationWrapper = new LambdaQueryWrapper<>();
        translationWrapper.in(Translation::getWordId, wordIdList);
        Map<Long, TranslationVo> translationVoMap = translationMapper.selectList(translationWrapper).stream()
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
            Long wordId = wordLVo.getWordId();
            TranslationVo translationVo = translationVoMap.get(wordId);
            wordLVo.setTranslation(translationVo);
            wordLVo.setForgetCount(wordIdFogetCOuntMap.get(wordId));
        }
        return ResponseResult.okResult(new WordListVo(wordLVos,statusList.get(0),page.getTotal()));
    }

    @Override
    public ResponseResult getWordDetails(Long id) {
        Word word = getWord(id);
        WordVo wordVo = BeanCopyUtils.copyBean(word, WordVo.class);
        wordVo.setPhrases(getPhrases(id));
        wordVo.setSentences(getSentences(id));
        wordVo.setTranslations(getTranslations(id));
        return ResponseResult.okResult(wordVo);
    }

    private Word getWord(Long id) {
        LambdaQueryWrapper<Word> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Word::getWordId, id);
        return wordMapper.selectOne(queryWrapper);
    }

    private  List<PhraseVo> getPhrases(Long id) {
        LambdaQueryWrapper<Phrase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Phrase::getWordId,id);
        List<Phrase> phrases = phraseMapper.selectList(queryWrapper);
        return BeanCopyUtils.copyBeanList(phrases, PhraseVo.class);
    }

    private  List<SentenceVo> getSentences(Long id) {
        LambdaQueryWrapper<Sentence> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Sentence::getWordId,id);
        List<Sentence> sentences = sentenceMapper.selectList(queryWrapper);
        return BeanCopyUtils.copyBeanList(sentences, SentenceVo.class);
    }

    private List<TranslationVo> getTranslations(Long id) {
        LambdaQueryWrapper<Translation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Translation::getWordId,id);
        List<Translation> translations = translationMapper.selectList(queryWrapper);
        return BeanCopyUtils.copyBeanList(translations, TranslationVo.class);
    }


}

