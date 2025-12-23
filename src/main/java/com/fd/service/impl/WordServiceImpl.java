package com.fd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fd.domain.ResponseResult;
import com.fd.domain.entity.Phrase;
import com.fd.domain.entity.Sentence;
import com.fd.domain.entity.Translation;
import com.fd.domain.entity.Word;
import com.fd.domain.vo.PhraseVo;
import com.fd.domain.vo.SentenceVo;
import com.fd.domain.vo.TranslationVo;
import com.fd.domain.vo.WordVo;
import com.fd.mapper.PhraseMapper;
import com.fd.mapper.SentenceMapper;
import com.fd.mapper.TranslationMapper;
import com.fd.mapper.WordMapper;
import com.fd.service.WordService;
import com.fd.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private PhraseMapper phraseMapper;

    @Autowired
    private SentenceMapper sentenceMapper;

    @Autowired
    private TranslationMapper translationMapper;
    @Override
    public ResponseResult listAll(Integer pageNum, Integer pageSize, Long id) {
        LambdaQueryWrapper<Word> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Word::getBookId, id);
        Page<Word> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<Word> words = page.getRecords();
        return null;
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

