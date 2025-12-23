package com.fd.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单词表(Word)表实体类
 *
 * @author makejava
 * @since 2025-12-22 11:23:10
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("word")
public class Word{
    //单词ID
    @TableId
    private Long wordId;
 
    //所属词书ID
    private Long bookId;
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





}

