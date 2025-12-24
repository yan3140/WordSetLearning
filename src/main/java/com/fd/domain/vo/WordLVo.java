package com.fd.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordLVo {
    //单词Id
    private Long wordId;
    //单词本身
    private String word;
    //释义
    private TranslationVo translation;
}
