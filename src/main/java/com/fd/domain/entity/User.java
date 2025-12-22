package com.fd.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表(User)表实体类
 *
 * @author makejava
 * @since 2025-12-22 13:09:48
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User{
    //用户ID
    @TableId(type = IdType.AUTO)
    private Long userId;
 
    //用户名
    private String userName;
    //昵称
    private String nickName;
    //性别
    private String sex;
    //邮箱
    private String email;
    //密码
    private String password;
    //头像
    private String avatar;
    //目标词书ID
    private Long targetBookId;
    //注册时间
    private Date createTime;





}

