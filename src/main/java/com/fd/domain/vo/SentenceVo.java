package com.fd.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SentenceVo {
    //英文句子
    private String enSentence;
    //中文句子
    private String cnSentence;

}
