package com.fd.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户测试表(UserExam)表实体类
 *
 * @author makejava
 * @since 2025-12-29 18:57:13
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_exam")
public class UserExam{
    //试卷ID
    @TableId(type = IdType.AUTO)
    private Long examId;
 
    //用户ID
    private Long userId;
    //单词书ID
    private Long bookId;
    //测试题目总数
    private Integer questionCount;
    //题型比例
    private Double questionRatio;
    //测试总时长（秒）
    private Integer examTimeLimit;
    //已用时间（秒）
    private Integer usedTime;
    //最终得分
    private Integer score;
    //状态：0-进行中 1-已完成
    private Integer status;
    //创建时间
    private Date createTime;
    //0-未删 1-已删
    private Integer isDeleted;





}

