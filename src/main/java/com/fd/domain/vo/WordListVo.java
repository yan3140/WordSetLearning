package com.fd.domain.vo;

import com.fd.domain.entity.Word;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordListVo {
    private List<WordLVo> rows;
    private Integer status;
    private Long total;
}
