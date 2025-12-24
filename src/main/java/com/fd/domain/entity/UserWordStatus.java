package com.fd.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户单词学习状态表(UserWordStatus)表实体类
 *
 * @author makejava
 * @since 2025-12-24 17:45:56
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_word_status")
public class UserWordStatus{
    //用户ID（关联user表）
    @TableId
    private Long userId;
    //单词ID（关联word表）
    @TableField("word_id")
    private Long wordId;
 
    //记忆状态（0-未记住，1-已记住，2-模糊）
    private Integer status;
    //不记得次数
    private Integer forgetCount;





}

