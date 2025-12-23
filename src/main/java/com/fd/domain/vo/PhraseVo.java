package com.fd.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhraseVo {
    //中文短语
    private String cnPhrase;
    //英文短语
    private String enPhrase;
}
