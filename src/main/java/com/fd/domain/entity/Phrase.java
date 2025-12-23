package com.fd.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短语表(Phrase)表实体类
 *
 * @author makejava
 * @since 2025-12-23 11:29:22
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("phrase")
public class Phrase{
    //短语ID
    @TableId
    private Long phraseId;
 
    //关联单词ID
    private Long wordId;
    //中文短语
    private String cnPhrase;
    //英文短语
    private String enPhrase;





}

