package com.fd.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 释义表(Translation)表实体类
 *
 * @author makejava
 * @since 2025-12-23 11:30:01
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("translation")
public class Translation{
    //释义ID
    @TableId
    private Long translationId;
 
    //关联单词ID
    private Long wordId;
    //英文释义
    private String enTran;
    //中文释义
    private String cnTran;
    //词性
    private String wordType;





}

