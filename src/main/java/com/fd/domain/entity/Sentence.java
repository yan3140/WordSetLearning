package com.fd.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 例句表(Sentence)表实体类
 *
 * @author makejava
 * @since 2025-12-23 11:29:43
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sentence")
public class Sentence{
    //例句ID
    @TableId
    private Long sentenceId;
 
    //关联单词ID
    private Long wordId;
    //英文句子
    private String enSentence;
    //中文句子
    private String cnSentence;





}

