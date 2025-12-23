package com.fd.domain.vo;

import com.fd.domain.entity.Phrase;
import com.fd.domain.entity.Sentence;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordVo {
    private Long wordId;
    //单词本身
    private String word;
    //英式音标
    private String ukPhone;
    //美式音标
    private String usPhone;
    //记忆方法
    private String remMethod;
    //配图URL
    private String picUrl;
    //释义
    private List<TranslationVo>  translations;
    //短语
    private List<PhraseVo> phrases;
    //句子
    private List<SentenceVo>  sentences;

}
