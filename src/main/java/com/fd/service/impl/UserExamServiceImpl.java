package com.fd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fd.domain.ResponseResult;
import com.fd.domain.constants.SystemConstants;
import com.fd.domain.dto.ExamDto;
import com.fd.domain.entity.ExamQuestion;
import com.fd.domain.entity.Translation;
import com.fd.domain.entity.UserExam;
import com.fd.domain.entity.Word;
import com.fd.domain.vo.QuestionVo;
import com.fd.domain.vo.TranslationVo;
import com.fd.domain.vo.WordLVo;
import com.fd.enums.AppHttpCodeEnum;
import com.fd.exception.SystemException;
import com.fd.mapper.TranslationMapper;
import com.fd.mapper.UserExamMapper;
import com.fd.service.ExamQuestionService;
import com.fd.service.UserExamService;
import com.fd.service.WordService;
import com.fd.utils.BeanCopyUtils;
import com.fd.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户测试表(UserExam)表服务实现类
 *
 * @author makejava
 * @since 2025-12-29 18:57:13
 */
@Service("userExamService")
public class UserExamServiceImpl extends ServiceImpl<UserExamMapper, UserExam> implements UserExamService {
    @Autowired
    private WordService wordService;

    @Autowired
    private TranslationMapper translationMapper;

    @Autowired
    private ExamQuestionService examQuestionService;

    @Override
    public ResponseResult getExams(Integer status) {
        Long userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<UserExam> queryWrapper = new LambdaQueryWrapper<UserExam>();
        queryWrapper.eq(UserExam::getStatus, status);
        queryWrapper.eq(UserExam::getUserId, userId);
        queryWrapper.eq(UserExam::getIsDeleted, SystemConstants.UNDELETED);
        List<UserExam> Exams = list(queryWrapper);
        return ResponseResult.okResult(Exams);
    }

    @Override
    public ResponseResult createExam(Long bookId, Integer questionCount, Double questionRatio, Integer examTimeLimit) {
        Long userId = SecurityUtils.getUserId();
        UserExam userExam = new UserExam();
        userExam.setUserId(userId);
        userExam.setBookId(bookId);
        userExam.setQuestionCount(questionCount);
        userExam.setExamTimeLimit(examTimeLimit);
        userExam.setQuestionRatio(questionRatio);
        save(userExam);
        Long examId = userExam.getExamId();
        LambdaQueryWrapper<UserExam> queryWrapper = new LambdaQueryWrapper<>();
        List<ExamQuestion> chineseQuestions =
                questions((int) ((1-questionRatio)*questionCount),SystemConstants.CHINESE_QUESTION,examId,bookId);
        List<ExamQuestion> englishQuestions =
                questions((int) (questionRatio * questionCount), SystemConstants.ENGLISH_QUESTION,examId,bookId);
        englishQuestions.addAll(chineseQuestions);
        for (int i = 1; i <= questionCount; i++) {
            englishQuestions.get(i-1).setQuestionId((long) i);
        }
        examQuestionService.saveBatch(englishQuestions);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateExamStatus(ExamDto examDto) {
        UserExam userExam = examDto.getUserExam();
        List<ExamQuestion> examQuestions = examDto.getExamQuestions();
        if(userExam.getUsedTime()<userExam.getExamTimeLimit()&&userExam.getStatus().equals(SystemConstants.EXAM_UNFINISHED)){
            boolean userExamUpdateSuccess = updateById(userExam);
            if (!userExamUpdateSuccess) {
                throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
            }
            examQuestionService.updateBatchById(examQuestions);
        }else{
            int correctCount = 0;
            for(ExamQuestion examQuestion : examQuestions){
                if(Objects.equals(examQuestion.getUserAnswer(), examQuestion.getCorrectAnswer())){
                    examQuestion.setIsCorrect(SystemConstants.CORRECT_ANSWER);
                    correctCount++;
                }else {
                    examQuestion.setIsCorrect(SystemConstants.WRONG_ANSWER);
                }
            }
            boolean examQuestionUpDateSuccess = examQuestionService.updateBatchById(examQuestions);
            if(!examQuestionUpDateSuccess){
                throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
            }
            userExam.setStatus(SystemConstants.EXAM_FINISHED);
            userExam.setScore(correctCount*100/examQuestions.size());
            updateById(userExam);
        }
        return ResponseResult.okResult();
    }

    private List<ExamQuestion> questions(Integer count, Integer type,Long examId,Long bookId) {
        LambdaQueryWrapper<Word> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Word::getBookId, bookId);
        long wordCount = wordService.count(queryWrapper);
        List<Long> wordIds = generateUniqueRandomNums(count * 4, wordCount);
        queryWrapper.in(Word::getWordId, wordIds);
        List<WordLVo> wordLVos = BeanCopyUtils.copyBeanList(wordService.list(queryWrapper), WordLVo.class);
        LambdaQueryWrapper<Translation> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(Translation::getWordId, wordIds);
        Map<Long, TranslationVo> translationVoMap = translationMapper.selectList(queryWrapper1).stream()
                .collect(Collectors.groupingBy(
                        Translation::getWordId,
                        Collectors.collectingAndThen(
                                Collectors.mapping(trans -> BeanCopyUtils.copyBean(trans, TranslationVo.class), Collectors.toList()),
                                list -> list.isEmpty() ? null : list.get(0)
                        )
                ));
        Map<Long,WordLVo> wordLVoMap = new HashMap<>();
        //wordId:wordLVo
        for (WordLVo wordLVo : wordLVos) {
            Long wordId = wordLVo.getWordId();
            TranslationVo translationVo = translationVoMap.get(wordId);
            wordLVo.setTranslation(translationVo);
            wordLVoMap.put(wordId, wordLVo);
        }
        List<ExamQuestion> questions = new ArrayList<>();
        if (type.equals(SystemConstants.CHINESE_QUESTION)) {
            for(int i = 0; i < count; i++){
                List<Long> options = wordIds.subList(i*4,i*4+4);
                List<String> finalOptions = putFirstNumToCorrectPosition(options).stream()
                        .map(option -> wordLVoMap.get(option).getWord())
                        .toList();
                String cnTran = wordLVoMap.get(options.get(0)).getTranslation().getCnTran();
                Integer l = Math.toIntExact(options.get(0) % 4);
                ExamQuestion examQuestion = new ExamQuestion(
                        null,
                        examId,
                        options.get(0),
                        type,
                        cnTran,
                        l,
                        finalOptions.get(0),
                        finalOptions.get(1),
                        finalOptions.get(2),
                        finalOptions.get(3),
                        null,
                        0);
                questions.add(examQuestion);
            }
        }else{
            for(int i = 0; i < count; i++){
                List<Long> options = wordIds.subList(i*4,i*4+4);
                List<String> finalOptions = putFirstNumToCorrectPosition(options).stream()
                        .map(option -> wordLVoMap.get(option).getTranslation().getCnTran())
                        .toList();
                String word =  wordLVoMap.get(options.get(0)).getWord();
                Integer l = Math.toIntExact(options.get(0) % 4);
                ExamQuestion examQuestion = new ExamQuestion(
                        null,
                        examId,
                        options.get(0),
                        type,
                        word,
                        l,
                        finalOptions.get(0),
                        finalOptions.get(1),
                        finalOptions.get(2),
                        finalOptions.get(3),
                        null,
                        0);
                questions.add(examQuestion);
            }
        }
        return questions;
    }

    private List<Long> generateUniqueRandomNums(int n, Long m) {
        List<Long> numPool = new ArrayList<>();
        for (Long i = 1L; i <= m; i++) {
            numPool.add(i);
        }
        Collections.shuffle(numPool);
        return numPool.subList(0, n);
    }

    public List<Long> putFirstNumToCorrectPosition(List<Long> list) {
        Long firstNum = list.get(0);
        int targetIdx = (int)(firstNum % 4);
        Long[] arr = new Long[4];
        arr[targetIdx] = firstNum;
        int fillIdx = 0;
        for (int i = 1; i < 4; i++) {
            while (arr[fillIdx] != null) fillIdx++;
            arr[fillIdx] = list.get(i);
        }
        return new ArrayList<>(Arrays.asList(arr));
    }

}

