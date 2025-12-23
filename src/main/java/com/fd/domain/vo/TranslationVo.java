package com.fd.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranslationVo {
    //英文释义
    private String enTran;
    //中文释义
    private String cnTran;
    //词性
    private String wordType;
}
